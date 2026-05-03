package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.minecraft.client.resource.Resource;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.client.resource.pack.ResourcePack;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
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
	public Resource getResource(Identifier location) throws IOException {
		return Adapters.resource(this.metadataSerializers, (NamespacedIdentifier) location, this.resourceManager.getResource((NamespacedIdentifier) location));
	}

	@Override
	public List<Resource> getResources(Identifier location) throws IOException {
		return Adapters.resources(this.metadataSerializers, this.resourceManager.getResourceStack((NamespacedIdentifier) location));
	}

	@Override
	public void addListener(net.minecraft.client.resource.manager.ResourceReloadListener listener) {
		this.resourceManager.addReloadedReloader(new WrappedResourceReloadListener(listener));
	}

	@Override
	public void reload(List<ResourcePack> packs) {
		this.resourceManager.reload(Adapters.resourcePacks(packs));
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
		public void resourcesReloaded(ResourceManager manager) {
			if (manager == ResourceManagerAdapter.this.resourceManager) {
				this.listener.reload(ResourceManagerAdapter.this);
			} else {
				throw new IllegalStateException("Woah there, who hijacked the resource manager?");
			}
		}
	}
}
