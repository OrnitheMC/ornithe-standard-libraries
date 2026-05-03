package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.resource.Identifier;
import net.minecraft.resource.Resource;
import net.minecraft.resource.pack.Pack;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;

public class ResourceManagerAdapter implements net.minecraft.resource.manager.ReloadableResourceManager {

	final SimpleReloadableResourceManager resourceManager;

	public ResourceManagerAdapter(SimpleReloadableResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Override
	public Set<String> getNamespaces() {
		return this.resourceManager.getNamespaces();
	}

	@Override
	public Resource getResource(Identifier location) throws IOException {
		return Adapters.resource((NamespacedIdentifier) location, this.resourceManager.getResource((NamespacedIdentifier) location));
	}

	@Override
	public List<Resource> getResources(Identifier location) throws IOException {
		return Adapters.resources(this.resourceManager.getResourceStack((NamespacedIdentifier) location));
	}

	@Override
	public Collection<Identifier> listResources(String directory, Predicate<String> filter) {
		return this.resourceManager.findResources(directory, location -> filter.test(location.identifier())).keySet().stream().map(Adapters::identifier).collect(Collectors.toList());
	}

	@Override
	public void addListener(net.minecraft.resource.manager.ResourceReloadListener listener) {
		this.resourceManager.addReloadedReloader(new WrappedResourceReloadListener(listener));
	}

	@Override
	public void reload(List<Pack> packs) {
		this.resourceManager.reload(Adapters.resourcePacks(packs));
	}

	private class WrappedResourceReloadListener implements ResourceReloadListener {

		private final net.minecraft.resource.manager.ResourceReloadListener listener;

		private WrappedResourceReloadListener(net.minecraft.resource.manager.ResourceReloadListener listener) {
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
