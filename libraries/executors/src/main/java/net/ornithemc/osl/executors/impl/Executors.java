package net.ornithemc.osl.executors.impl;

import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Executors {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Executors");

	private static final ExecutorService BACKGROUND_EXECUTOR = makeExecutor("Main");

	private static ExecutorService makeExecutor(String name) {
		int maxBackgroundThreads = 7;
		int backgroundThreads = Math.min(Runtime.getRuntime().availableProcessors() - 1, maxBackgroundThreads);

		AtomicInteger workerCount = new AtomicInteger(1);

		return new ForkJoinPool(backgroundThreads, forkJoinPool -> {
			ForkJoinWorkerThread backgroundThread = new ForkJoinWorkerThread(forkJoinPool) {

				@Override
				protected void onTermination(Throwable exception) {
					if (exception != null) {
						LOGGER.warn("{} died", this.getName(), exception);
					} else {
						LOGGER.debug("{} shutdown", this.getName());
					}

					super.onTermination(exception);
				}
			};
			backgroundThread.setName("Worker-" + workerCount.getAndIncrement());
			return backgroundThread;
		}, Executors::handleBackgroundThreadException, true);
	}

	private static void handleBackgroundThreadException(Thread thread, Throwable exception) {
		if (exception instanceof CompletionException) {
			exception = exception.getCause();
		}

		LOGGER.error("Caught exception in thread {}", thread, exception);
	}

	public static Executor backgroundExecutor() {
		return BACKGROUND_EXECUTOR;
	}

	public static void shutdownBackgroundExecutor() {
		BACKGROUND_EXECUTOR.shutdown();

		try {
			boolean terminated = BACKGROUND_EXECUTOR.awaitTermination(3, TimeUnit.SECONDS);

			if (!terminated) {
				BACKGROUND_EXECUTOR.shutdownNow();
			}
		} catch (InterruptedException ignored) {
		}
	}
}
