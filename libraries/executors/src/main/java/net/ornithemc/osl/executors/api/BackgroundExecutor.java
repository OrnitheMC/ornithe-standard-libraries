package net.ornithemc.osl.executors.api;

import java.util.concurrent.Executor;

import net.ornithemc.osl.executors.impl.Executors;

/**
 * A utility class for accessing the background executor.
 * 
 * <p>
 * The background executor can be used to run tasks asynchronously.
 * This will take load off the game's main thread, which can prevent
 * excessive slow-downs.
 * 
 * <p>
 * Do note that operations done on background threads must be thread-
 * safe. Do NOT modify the world or render state in background tasks,
 * as this will inevitably lead to save corruption and crashes.
 */
public final class BackgroundExecutor {

	/**
	 * @return the background executor.
	 */
	public static Executor get() {
		return Executors.backgroundExecutor();
	}
}
