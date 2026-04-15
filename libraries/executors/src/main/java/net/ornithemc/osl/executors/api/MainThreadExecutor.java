package net.ornithemc.osl.executors.api;

import java.util.concurrent.Executor;

// default  method implementations are necessary
// for the transitive interface injection to work
public interface MainThreadExecutor extends Executor {

	@Override
	default void execute(Runnable task) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return whether this executor is running on the same thread as the caller.
	 */
	default boolean isRunningOnSameThread() {
		throw new UnsupportedOperationException();
	}
}
