package net.ornithemc.osl.resource.loader.impl.resource.reload;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import net.ornithemc.osl.core.api.util.Unit;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ReloadStep;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReload;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader.SharedState;

public class SimpleResourceReload<R> implements ResourceReload {

	public static ResourceReload start(ResourceManager manager, List<ResourceReloader> reloaders, Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<?> initialTask) {
		SimpleResourceReload<Void> reload = new SimpleResourceReload<>();
		reload.start(manager, reloaders, backgroundExecutor, mainThreadExecutor, TaskFactory.SIMPLE, initialTask);
		return reload;
	}

	private static final int RELOAD_PROGRESS_WEIGHT = 2;
	private static final int APPLICATION_PROGRESS_WEIGHT = 2;
	private static final int RELOADER_PROGRESS_WEIGHT = 1;

	private final Set<ResourceReloader> runningReloaders = new HashSet<>();
	private final CompletableFuture<Unit> reloads = new CompletableFuture<>();

	private final AtomicInteger startedReloads = new AtomicInteger();
	private final AtomicInteger finishedReloads = new AtomicInteger();
	private final AtomicInteger startedApplications = new AtomicInteger();
	private final AtomicInteger finishedApplications = new AtomicInteger();

	private int reloaderCount;
	private CompletableFuture<List<R>> result;

	void start(ResourceManager manager, List<ResourceReloader> reloaders, Executor backgroundExecutor, Executor mainThreadExecutor, TaskFactory<R> taskFactory, CompletableFuture<?> initialTask) {
		this.runningReloaders.addAll(reloaders);
		this.reloaderCount = reloaders.size();

		this.result = this.startTasks(manager, reloaders, backgroundExecutor, mainThreadExecutor, taskFactory, initialTask);
	}

	CompletableFuture<List<R>> startTasks(ResourceManager manager, List<ResourceReloader> reloaders, Executor backgroundExecutor, Executor mainThreadExecutor, TaskFactory<R> taskFactory, CompletableFuture<?> initialTask) {
		Executor reloadExecutor = r -> {
			this.startedReloads.incrementAndGet();
			backgroundExecutor.execute(() -> {
				r.run();
				this.finishedReloads.incrementAndGet();
			});
		};
		Executor applyExecutor = r -> {
			this.startedApplications.incrementAndGet();
			mainThreadExecutor.execute(() -> {
				r.run();
				this.finishedApplications.incrementAndGet();
			});
		};

		this.startedReloads.incrementAndGet();
		initialTask.thenRun(this.finishedReloads::incrementAndGet);

		SharedState state = new SharedReloaderState(manager);

		for (ResourceReloader reloader : reloaders) {
			reloader.prepareSharedState(state);
		}

		List<CompletableFuture<R>> tasks = new ArrayList<>();
		CompletableFuture<?> prevTask = initialTask;

		for (ResourceReloader reloader : reloaders) {
			ReloadStep previousStep = this.createReloadStep(reloader, prevTask, mainThreadExecutor);
			CompletableFuture<R> task = taskFactory.createTask(reloader, state, previousStep, reloadExecutor, applyExecutor);

			tasks.add(task);
			prevTask = task;
		}

		return Util.sequence(tasks);
	}

	private ReloadStep createReloadStep(ResourceReloader reloader, CompletableFuture<?> task, Executor mainThreadExecutor) {
		return new ReloadStep() {

			@Override
			public <T> CompletableFuture<T> await(T t) {
				mainThreadExecutor.execute(() -> {
					SimpleResourceReload.this.runningReloaders.remove(reloader);
					if (SimpleResourceReload.this.runningReloaders.isEmpty()) {
						SimpleResourceReload.this.reloads.complete(Unit.INSTANCE);
					}
				});

				return SimpleResourceReload.this.reloads.thenCombine(task, (p, b) -> t);
			}
		};
	}

	@Override
	public CompletableFuture<Unit> result() {
		return Objects.requireNonNull(this.result, "Resource reload has not started!").thenApply(s -> Unit.INSTANCE);
	}

	@Override
	public float getProgress() {
		float weightedFinished = weightProgress(this.finishedReloads.get(), this.finishedApplications.get(), this.reloaderCount - this.runningReloaders.size());
		float weightedTotal = weightProgress(this.startedReloads.get(), this.startedApplications.get(), this.reloaderCount);

		return weightedFinished / weightedTotal;
	}

	@Override
	public boolean isApplying() {
		return this.reloads.isDone();
	}

	private static int weightProgress(int reloads, int applications, int reloaders) {
		return reloads * RELOAD_PROGRESS_WEIGHT + applications * APPLICATION_PROGRESS_WEIGHT + reloaders * RELOADER_PROGRESS_WEIGHT;
	}

	interface TaskFactory<R> {

		TaskFactory<Void> SIMPLE = ResourceReloader::reloadResources;

		CompletableFuture<R> createTask(ResourceReloader reloader, SharedState state, ReloadStep previousStep, Executor reloadExecutor, Executor applyExecutor);

	}
}
