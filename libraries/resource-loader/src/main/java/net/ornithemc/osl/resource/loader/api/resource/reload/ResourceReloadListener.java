package net.ornithemc.osl.resource.loader.api.resource.reload;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.ornithemc.osl.core.api.util.Unit;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

/**
 * A resource reloader that runs after the reload stage, in the application stage.
 */
public interface ResourceReloadListener extends ResourceReloader {

	@Override
	default void prepareSharedState(SharedState state) {
	}

	@Override
	default CompletableFuture<Void> reloadResources(SharedState state, ReloadStep previousStep, Executor reloadExecutor, Executor applyExecutor) {
		return previousStep.await(Unit.INSTANCE).thenRunAsync(() -> {
			this.resourcesReloaded(state.resourceManager());
		}, applyExecutor);
	}

	void resourcesReloaded(ResourceManager manager);

}
