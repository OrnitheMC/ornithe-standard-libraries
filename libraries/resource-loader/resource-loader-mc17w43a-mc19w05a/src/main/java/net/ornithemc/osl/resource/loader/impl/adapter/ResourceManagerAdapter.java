package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.resource.Identifier;
import net.minecraft.resource.pack.Pack;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;

public class ResourceManagerAdapter implements net.minecraft.resource.manager.ReloadableResourceManager {

	private final SimpleReloadableResourceManager resourceManager;

	public ResourceManagerAdapter(SimpleReloadableResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Override
	public Set<String> getNamespaces() {
		return this.resourceManager.getNamespaces();
	}

	@Override
	public net.minecraft.resource.Resource getResource(Identifier location) throws IOException {
		return this.wrapResource(location, this.resourceManager.getResource(location));
	}

	@Override
	public List<net.minecraft.resource.Resource> getResources(Identifier location) throws IOException {
		return this.wrapResources(this.resourceManager.getResourceStack(location));
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
		this.resourceManager.reload(this.wrapPacks(packs));
	}

	private net.minecraft.resource.Resource wrapResource(NamespacedIdentifier location, Optional<Resource> resource) throws IOException {
		if (resource.isPresent()) {
			return new ResourceAdapter(resource.get());
		}

		throw new FileNotFoundException(location.toString());
	}

	private List<net.minecraft.resource.Resource> wrapResources(List<Resource> resources) throws IOException {
		List<net.minecraft.resource.Resource> wrappedResources = new ArrayList<>();

		for (Resource resource : resources) {
			wrappedResources.add(new ResourceAdapter(resource));
		}

		return Collections.unmodifiableList(wrappedResources);
	}

	private ResourcePack wrapPack(Pack pack) {
		if (pack instanceof ResourcePackAdapter) {
			return ((ResourcePackAdapter) pack).pack;
		} else {
			return new WrappedPack(pack);
		}
	}

	private List<ResourcePack> wrapPacks(List<Pack> packs) {
		return packs.stream().map(this::wrapPack).collect(Collectors.toList());
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
