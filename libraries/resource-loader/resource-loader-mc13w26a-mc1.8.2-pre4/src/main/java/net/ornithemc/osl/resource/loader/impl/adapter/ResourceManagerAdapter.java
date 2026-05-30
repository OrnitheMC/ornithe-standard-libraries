package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;

public class ResourceManagerAdapter implements net.minecraft.client.resource.manager.ReloadableResourceManager {

	private final ResourceMetadataSerializerRegistry metadataSerializers;
	private final SimpleReloadableResourceManager resourceManager;

	public ResourceManagerAdapter(ResourceMetadataSerializerRegistry metadataSerializers, SimpleReloadableResourceManager resourceManager) {
		this.metadataSerializers = metadataSerializers;
		this.resourceManager = resourceManager;
	}

	@Override
	public Set<String> getNamespaces() {
		return this.resourceManager.getNamespaces();
	}

	@Override
	public net.minecraft.client.resource.Resource getResource(Identifier location) throws IOException {
		return this.wrapResource(location, this.resourceManager.getResource(location));
	}

	@Override
	public List<net.minecraft.client.resource.Resource> getResources(Identifier location) throws IOException {
		return this.wrapResources(this.resourceManager.getResourceStack(location));
	}

	@Override
	public void addListener(net.minecraft.client.resource.manager.ResourceReloadListener listener) {
		this.resourceManager.addReloadedReloader(new WrappedResourceReloadListener(listener));
	}

	@Override
	public void reload(List<net.minecraft.client.resource.pack.ResourcePack> packs) {
		this.resourceManager.reload(this.wrapResourcePacks(packs));
	}

	private net.minecraft.client.resource.Resource wrapResource(NamespacedIdentifier location, Optional<Resource> resource) throws IOException {
		if (resource.isPresent()) {
			return new ResourceAdapter(this.metadataSerializers, resource.get());
		}

		throw new FileNotFoundException(location.toString());
	}

	private List<net.minecraft.client.resource.Resource> wrapResources(List<Resource> resources) throws IOException {
		List<net.minecraft.client.resource.Resource> wrappedResources = new ArrayList<>();

		for (Resource resource : resources) {
			wrappedResources.add(new ResourceAdapter(this.metadataSerializers, resource));
		}

		return Collections.unmodifiableList(wrappedResources);
	}

	private ResourcePack wrapResourcePack(net.minecraft.client.resource.pack.ResourcePack pack) {
		if (pack instanceof ResourcePackAdapter) {
			return ((ResourcePackAdapter) pack).pack;
		} else {
			return new WrappedResourcePack(pack);
		}
	}

	private List<ResourcePack> wrapResourcePacks(List<net.minecraft.client.resource.pack.ResourcePack> packs) {
		return packs.stream().map(this::wrapResourcePack).collect(Collectors.toList());
	}

	private class WrappedResourceReloadListener implements ResourceReloadListener {

		private final net.minecraft.client.resource.manager.ResourceReloadListener listener;

		private WrappedResourceReloadListener(net.minecraft.client.resource.manager.ResourceReloadListener listener) {
			this.listener = listener;

			// Vanilla's reload listeners are triggered immediately after registering
			// We must emulate this or the game's initialization will fail
			this.listener.reload(ResourceManagerAdapter.this);
		}

		@Override
		public String getName() {
			return this.listener.getClass().getSimpleName();
		}

		@Override
		public void resourcesReloaded(ResourceManager manager) {
			if (manager == ResourceManagerAdapter.this.resourceManager) {
				this.listener.reload(ResourceManagerAdapter.this);
			} else {
				throw new IllegalStateException("Woah there, who hijacked the resource manager?");
			}
		}
	}
}
