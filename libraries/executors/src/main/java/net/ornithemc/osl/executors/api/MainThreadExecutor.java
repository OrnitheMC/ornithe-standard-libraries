package net.ornithemc.osl.executors.api;

import java.util.concurrent.Executor;

public interface MainThreadExecutor extends Executor {

	/**
	 * @return whether this executor is on the same thread as the caller.
	 */
	boolean isOnSameThread();

}
