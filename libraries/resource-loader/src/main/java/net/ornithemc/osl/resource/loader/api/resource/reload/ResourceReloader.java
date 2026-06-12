package net.ornithemc.osl.resource.loader.api.resource.reload;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

/**
 * A resource reloader handles the actual reloading of specific resources.
 */
public interface ResourceReloader {

	/**
	 * @return the name of this resource reloader for logging purposes.
	 */
	default String getName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Prepares the {@linkplain SharedState} that is shared between all reloaders of a resource reload.
	 * 
	 * @param state the shared state to be prepared.
	 */
	void prepareSharedState(SharedState state);

	/**
	 * @param state          the shared state of the resource reload.
	 * @param previousStep   the previous reload step that must finish before resources can be applied.
	 * @param reloadExecutor the executor that runs the reload steps.
	 * @param applyExecutor  the executor that runs the application steps.
	 * @return the future of the reload performed by this reloader.
	 */
	CompletableFuture<Void> reloadResources(SharedState state, ReloadStep previousStep, Executor reloadExecutor, Executor applyExecutor);

	/**
	 * A {@code SharedState} holds generic key-value pairs.
	 */
	interface SharedState {

		/**
		 * @return the resource manager of this shared state.
		 */
		ResourceManager resourceManager();

		/**
		 * Inserts the given key-value pair into this shared state.
		 * 
		 * @param <T>   the value type.
		 * @param key   the key of the pair.
		 * @param value the value of the pair.
		 */
		<T> void set(Key<T> key, T value);

		/**
		 * Retrieves the value paired with the given key from this shared state.
		 * 
		 * @param <T> the value type.
		 * @param key the key of the key-value pair.
		 * @return the value paired with the given key.
		 */
		<T> T get(Key<T> key);

		/**
		 * Represents a key from a key-value pair from a {@linkplain SharedState}.
		 * 
		 * @param <T> the value type.
		 */
		interface Key<T> {
		}
	}
}
