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

	private static final String MAX_BACKGROUND_THREADS_PROPERTY= "max.bg.threads";
	private static final int MAX_BACKGROUND_THREADS_LIMIT = 255;

	private static final ExecutorService BACKGROUND_EXECUTOR = makeExecutor("OSL");

	private static ExecutorService makeExecutor(String name) {
		int maxBackgroundThreads = maxBackgroundThreads();
		int backgroundThreads = allowedBackgroundThreads(maxBackgroundThreads);

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
			backgroundThread.setName(String.format("Worker-%s-%d", name, workerCount.getAndIncrement()));
			return backgroundThread;
		}, Executors::handleBackgroundThreadException, true);
	}

	private static int maxBackgroundThreads() {
		String s = System.getProperty(MAX_BACKGROUND_THREADS_PROPERTY);

		if (s != null) {
			try {
				int maxThreads = Integer.parseInt(s);

				if (maxThreads >= 1 && maxThreads <= MAX_BACKGROUND_THREADS_LIMIT) {
					return maxThreads;
				}

				LOGGER.error("Wrong {} property value '{}'. Should be an integer value between 1 and {}.", "max.bg.threads", s, MAX_BACKGROUND_THREADS_LIMIT);
			} catch (NumberFormatException var2) {
				LOGGER.error("Could not parse {} property value '{}'. Should be an integer value between 1 and {}.", MAX_BACKGROUND_THREADS_PROPERTY, s, MAX_BACKGROUND_THREADS_LIMIT);
			}
		}

		return MAX_BACKGROUND_THREADS_LIMIT;
	}

	private static int allowedBackgroundThreads(int max) {
		return Math.min(Runtime.getRuntime().availableProcessors() - 1, max);
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
