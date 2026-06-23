package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.client.resource.pack.BuiltInResourcePack;
import net.minecraft.client.resource.pack.CustomResourcePack;
import net.minecraft.client.resource.pack.DirectoryResourcePack;
import net.minecraft.client.resource.pack.LegacyResourcePack;
import net.minecraft.client.resource.pack.ZippedResourcePack;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.AbstractResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourceConsumer;
import net.ornithemc.osl.resource.loader.impl.mixin.client.CustomResourcePackAccess;
import net.ornithemc.osl.resource.loader.impl.mixin.client.LegacyResourcePackAccess;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

public class WrappedResourcePack extends AbstractResourcePack {

	final net.minecraft.client.resource.pack.ResourcePack pack;

	public WrappedResourcePack(net.minecraft.client.resource.pack.ResourcePack pack) {
		this.pack = pack;
	}

	@Override
	public String getName() {
		return this.pack.getName();
	}

	@Override
	public boolean hasResource(String path) {
		return this.hasResourceFromPack(this.pack, path);
	}

	private boolean hasResourceFromPack(net.minecraft.client.resource.pack.ResourcePack pack, String path) {
		if (ResourcePacks.getSupportedFormat() > 2 && pack instanceof LegacyResourcePack) {
			return this.hasResourceFromPack(((LegacyResourcePackAccess) pack).accessPack(), path);
		} else if (pack instanceof BuiltInResourcePack) {
			return ((BuiltInResourcePack) pack).hasResource(new FilePathIdentifier(path)) 
				|| ((BuiltInResourcePack) pack).hasResource(new Identifier(path));
		} else if (pack instanceof CustomResourcePack) {
			return ((CustomResourcePackAccess) pack).invokeHasResource(path);
		} else if (pack instanceof ResourcePackAdapter) {
			return ((ResourcePackAdapter) pack).pack.hasResource(path);
		} else {
			return false;
		}
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		return this.getResourceFromPack(this.pack, path);
	}

	private InputStream getResourceFromPack(net.minecraft.client.resource.pack.ResourcePack pack, String path) throws IOException {
		if (ResourcePacks.getSupportedFormat() > 2 && pack instanceof LegacyResourcePack) {
			return this.getResourceFromPack(((LegacyResourcePackAccess) pack).accessPack(), path);
		} else if (pack instanceof BuiltInResourcePack) {
			return ((BuiltInResourcePack) pack).hasResource(new FilePathIdentifier(path))
				? ((BuiltInResourcePack) pack).getResource(new FilePathIdentifier(path))
				: ((BuiltInResourcePack) pack).getResource(new Identifier(path));
		} else if (pack instanceof CustomResourcePack) {
			return ((CustomResourcePackAccess) pack).invokeOpenResource(path);
		} else if (pack instanceof ResourcePackAdapter) {
			return ((ResourcePackAdapter) pack).pack.getResource(path);
		} else {
			return null;
		}
	}

	@Override
	protected Map<ResourceType, Set<String>> findNamespaces() {
		return new EnumMap<>(Collections.singletonMap(ResourceType.CLIENT_ASSETS, this.pack.getNamespaces()));
	}

	@Override
	public boolean hasResource(ResourceType type, NamespacedIdentifier location) {
		return type == ResourceType.CLIENT_ASSETS && this.pack.hasResource(Adapters.identifier(location));
	}

	@Override
	public IOSupplier<InputStream> getResource(ResourceType type, NamespacedIdentifier location) {
		return type == ResourceType.CLIENT_ASSETS && this.pack.hasResource(Adapters.identifier(location)) ? () -> this.pack.getResource(Adapters.identifier(location)) : null;
	}

	@Override
	public void findResources(ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
		if (type == ResourceType.CLIENT_ASSETS) {
			this.findResourcesInPack(this.pack, type, namespace, directory, consumer);
		}
	}

	private void findResourcesInPack(net.minecraft.client.resource.pack.ResourcePack pack, ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
		if (ResourcePacks.getSupportedFormat() > 2 && pack instanceof LegacyResourcePack) {
			this.findResourcesInPack(((LegacyResourcePackAccess) pack).accessPack(), type, namespace, directory, consumer);
		} else if (pack instanceof BuiltInResourcePack) {
			ModContainer mod = FabricLoader.getInstance().getModContainer("minecraft").get();
			List<Path> rootPaths = ResourcePacks.getRootPaths(mod.getRootPaths(), ".");

			ResourcePacks.findResources(this, rootPaths, type, namespace, directory, consumer);
		} else if (pack instanceof DirectoryResourcePack) {
			CustomResourcePackAccess directoryPack = (CustomResourcePackAccess) pack;
			List<Path> rootPaths = ResourcePacks.getRootPaths(directoryPack.accessFile().toPath(), ".");

			if (!rootPaths.isEmpty()) {
				ResourcePacks.findResources(this, rootPaths, type, namespace, directory, consumer);
			}
		} else if (pack instanceof ZippedResourcePack) {
			CustomResourcePackAccess zippedPack = (CustomResourcePackAccess) pack;

			try (FileSystem zipFs = FileSystems.newFileSystem(zippedPack.accessFile().toPath(), (ClassLoader) null)) {
				List<Path> rootPaths = ResourcePacks.getRootPaths(zipFs.getRootDirectories(), ".");

				if (!rootPaths.isEmpty()) {
					ResourcePacks.findResources(this, rootPaths, type, namespace, directory, consumer);
				}
			} catch (IOException e) {
			}
		} else if (pack instanceof ResourcePackAdapter) {
			((ResourcePackAdapter) pack).pack.findResources(type, namespace, directory, consumer);
		}
	}
}
