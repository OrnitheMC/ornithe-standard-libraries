package net.ornithemc.osl.resource.loader.api.resource.reload;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.ornithemc.osl.core.api.util.Unit;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.resource.reload.ProfiledResourceReload;
import net.ornithemc.osl.resource.loader.impl.resource.reload.SimpleResourceReload;

/**
 * Represents a resource reload.
 */
public interface ResourceReload {

	/**
	 * Starts a resource reload with the given resource reloaders.
	 * 
	 * @return a new resource reload.
	 */
	static ResourceReload start(ResourceManager manager, List<ResourceReloader> reloaders, Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<?> initialTask) {
		if (ResourceLoader.LOGGER.isDebugEnabled()) {
			return ProfiledResourceReload.start(manager, reloaders, backgroundExecutor, mainThreadExecutor, initialTask);
		} else {
			return SimpleResourceReload.start(manager, reloaders, backgroundExecutor, mainThreadExecutor, initialTask);
		}
	}

	/**
	 * @return the result future of this resource reload.
	 */
	CompletableFuture<Unit> result();

	/**
	 * @return the progress of this resource reload, as a number between 0 and 1.
	 */
	float getProgress();

	/**
	 * @return whether this resource reload is already in the application stage.
	 */
	boolean isApplying();

	/**
	 * @return whether this resource reload is finished.
	 */
	default boolean isDone() {
		return this.result().isDone();
	}

	/**
	 * Checks whether this resource reload has any exceptions.
	 * If so, an unchecked exception is thrown.
	 */
	default void checkExceptions() {
		CompletableFuture<Unit> result = this.result();

		if (result.isCompletedExceptionally()) {
			result.join();
		}
	}
}
