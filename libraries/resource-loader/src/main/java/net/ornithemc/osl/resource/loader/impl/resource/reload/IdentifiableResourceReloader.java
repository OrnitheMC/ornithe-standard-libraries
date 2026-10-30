package net.ornithemc.osl.resource.loader.impl.resource.reload;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.reload.ReloadStep;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;

public final class IdentifiableResourceReloader implements ResourceReloader  {

	private final NamespacedIdentifier identifier;
	private final ResourceReloader reloader;

	public IdentifiableResourceReloader(NamespacedIdentifier identifier, ResourceReloader reloader) {
		this.identifier = identifier;
		this.reloader = reloader;
	}

	public NamespacedIdentifier getIdentifier() {
		return this.identifier;
	}

	@Override
	public String getName() {
		return this.identifier.toString();
	}

	@Override
	public void prepareSharedState(SharedState state) {
		this.reloader.prepareSharedState(state);
	}

	@Override
	public CompletableFuture<Void> reloadResources(SharedState state, ReloadStep previousStep, Executor reloadExecutor, Executor applyExecutor) {
		return this.reloader.reloadResources(state, previousStep, reloadExecutor, applyExecutor);
	}
}
