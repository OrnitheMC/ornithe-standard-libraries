package net.ornithemc.osl.resource.loader.impl.resource.reload;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.base.Stopwatch;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReload;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

public class ProfiledResourceReload extends SimpleResourceReload<ProfiledResourceReload.ProfileResult> {

	public static ResourceReload start(ResourceManager manager, List<ResourceReloader> reloaders, Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<?> initialTask, Runnable finalTask) {
		ProfiledResourceReload reload = new ProfiledResourceReload();
		reload.start(manager, reloaders, backgroundExecutor, mainThreadExecutor, TASK_FACTORY, initialTask, finalTask);
		return reload;
	}

	private static final TaskFactory<ProfileResult> TASK_FACTORY = (reloader, state, previousStep, reloadExecutor, applyExecutor) -> {
		ProfileResult result = new ProfileResult(reloader.getName());
		reloadExecutor = profileExecutor(reloadExecutor, result.reloadTaskCount, result.reloadTimeNanos);
		applyExecutor = profileExecutor(applyExecutor, result.applicationTaskCount, result.applicationTimeNanos);

		return reloader.reloadResources(state, previousStep, reloadExecutor, applyExecutor).thenApply(v -> result);
	};

	private static Executor profileExecutor(Executor executor, AtomicLong taskCount, AtomicLong timeNanos) {
		return task -> executor.execute(() -> {
			long startNanos = System.nanoTime();
			task.run();
			long endNanos = System.nanoTime();

			taskCount.incrementAndGet();
			timeNanos.addAndGet(endNanos - startNanos);
		});
	}

	private final Stopwatch stopwatch = Stopwatch.createStarted();

	@Override
	CompletableFuture<List<ProfileResult>> startTasks(ResourceManager manager, List<ResourceReloader> reloaders, Executor backgroundExecutor, Executor mainThreadExecutor, TaskFactory<ProfileResult> taskFactory, CompletableFuture<?> initialTask) {
		return super.startTasks(manager, reloaders, backgroundExecutor, mainThreadExecutor, taskFactory, initialTask).thenApplyAsync(this::finish, mainThreadExecutor);
	}

	private List<ProfileResult> finish(List<ProfileResult> results) {
		this.stopwatch.stop();

		long blockingTasks = 0L;
		long blockingTime = 0L;

		ResourceLoader.LOGGER.info("Resource Reload finished in {} ms", this.stopwatch.elapsed(TimeUnit.MILLISECONDS));

		for (ProfileResult result : results) {
			String reloaderName = result.reloaderName;
			long reloadTasks = result.reloadTaskCount.get();
			long reloadTime = result.reloadTimeNanos.get() / 1000000;
			long applicationTasks = result.applicationTaskCount.get();
			long applicationTime = result.applicationTimeNanos.get() / 1000000;

			ResourceLoader.LOGGER.info(
				"{} took approximately {} tasks/{} ms ({} tasks/{} ms reloading, {} tasks/{} ms applying)",
				reloaderName,
				reloadTasks + applicationTasks, reloadTime + applicationTime,
				reloadTasks, reloadTime,
				applicationTasks, applicationTime
			);

			blockingTasks += applicationTasks;
			blockingTime += applicationTime;
		}

		ResourceLoader.LOGGER.info("Blocking {} tasks/{} ms", blockingTasks, blockingTime);

		return results;
	}

	static class ProfileResult {

		final String reloaderName;
		final AtomicLong reloadTaskCount = new AtomicLong();
		final AtomicLong reloadTimeNanos = new AtomicLong();
		final AtomicLong applicationTaskCount = new AtomicLong();
		final AtomicLong applicationTimeNanos = new AtomicLong();

		ProfileResult(String reloaderName) {
			this.reloaderName = reloaderName;
		}
	}
}
