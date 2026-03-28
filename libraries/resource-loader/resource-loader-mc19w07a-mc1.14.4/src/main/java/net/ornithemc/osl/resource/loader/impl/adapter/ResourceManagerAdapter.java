package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.resource.Identifier;
import net.minecraft.resource.Resource;
import net.minecraft.resource.pack.Pack;
import net.minecraft.unmapped.C_17695012;
import net.minecraft.unmapped.C_75765617;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.reload.ReloadStep;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;

public class ResourceManagerAdapter implements net.minecraft.resource.manager.ReloadableResourceManager {

	final SimpleReloadableResourceManager resourceManager;

	public ResourceManagerAdapter(SimpleReloadableResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Override
	public void add(Pack pack) {
		this.resourceManager.add(Adapters.resourcePack(pack));
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
	public boolean m_12351820(Identifier location) {
		return this.resourceManager.hasResource((NamespacedIdentifier) location);
	}

	@Override
	public void addListener(net.minecraft.resource.manager.ResourceReloadListener listener) {
		this.resourceManager.addReloader(new WrappedResourceReloadListener(listener));
	}

	@Override
	public C_17695012 m_24284863(Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<C_75765617> initialTask) {
		return new ResourceReloadAdapter(this.resourceManager.startPartialReload(backgroundExecutor, mainThreadExecutor, initialTask));
	}

	@Override
	public C_17695012 m_73356589(Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<C_75765617> initialTask, List<Pack> selectedPacks) {
		return new ResourceReloadAdapter(this.resourceManager.startReload(Adapters.resourcePacks(selectedPacks), backgroundExecutor, mainThreadExecutor, initialTask));
	}

	@Override
	public CompletableFuture<C_75765617> reload(Executor backgroundExecutor, Executor mainThreadExecutor, List<Pack> selectedPacks, CompletableFuture<C_75765617> initialTask) {
		return this.resourceManager.startReload(Adapters.resourcePacks(selectedPacks), backgroundExecutor, mainThreadExecutor, initialTask).result().thenApply(u -> C_75765617.INSTANCE);
	}

	private class WrappedResourceReloadListener implements ResourceReloader {

		private final net.minecraft.resource.manager.ResourceReloadListener listener;

		private WrappedResourceReloadListener(net.minecraft.resource.manager.ResourceReloadListener listener) {
			this.listener = listener;
		}

		@Override
		public void prepareSharedState(SharedState state) {
		}

		@Override
		public CompletableFuture<Void> reloadResources(SharedState state, ReloadStep previousStep, Executor reloadExecutor, Executor applyExecutor) {
			if (state.resourceManager() == ResourceManagerAdapter.this.resourceManager) {
				return this.listener.m_24588393(previousStep::await, ResourceManagerAdapter.this, net.minecraft.unmapped.C_45729948.f_29046737, net.minecraft.unmapped.C_45729948.f_29046737, reloadExecutor, applyExecutor);
			} else {
				throw new IllegalStateException("Woah there, who hijacked the resource manager?");
			}
		}
	}
}
