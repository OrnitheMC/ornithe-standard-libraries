package net.ornithemc.osl.resource.loader.api.resource.reload;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

/**
 * A simple, two-stage resource reloader.
 * 
 * @param <T> the type of resources to be reloaded.
 */
public interface SimpleResourceReloader<T> extends ResourceReloader {

	@Override
	default void prepareSharedState(SharedState state) {
	}

	@Override
	default CompletableFuture<Void> reloadResources(SharedState state, ReloadStep previousStep, Executor reloadExecutor, Executor applyExecutor) {
		return CompletableFuture
				.supplyAsync(() -> this.reloadResources(state.resourceManager()), reloadExecutor)
				.thenCompose(previousStep::await)
				.thenAcceptAsync(resources -> this.applyResources(resources, state.resourceManager()), applyExecutor);
	}

	/**
	 * Performs the reload stage for this resource reloader.
	 * <p>
	 * This stage may not run on the main game thread, so it is
	 * imperative that the world or render state is not modified.
	 * 
	 * @param manager
	 * @return the reloaded resources.
	 */
	T reloadResources(ResourceManager manager);

	/**
	 * Performs the application stage for this resource reloader.
	 * 
	 * @param resources the resources to apply.
	 * @param manager 
	 */
	void applyResources(T resources, ResourceManager manager);

}
