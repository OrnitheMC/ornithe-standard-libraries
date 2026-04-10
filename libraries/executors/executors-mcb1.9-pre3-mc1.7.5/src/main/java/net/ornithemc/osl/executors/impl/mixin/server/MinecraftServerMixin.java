package net.ornithemc.osl.executors.impl.mixin.server;

import java.util.ArrayDeque;
import java.util.Queue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.executors.api.Executors;
import net.ornithemc.osl.executors.api.MainThreadExecutor;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MainThreadExecutor {

	@Unique
	private final Queue<Runnable> pendingTasks = new ArrayDeque<>();

	@Unique
	private Thread thread;

	@Inject(
		method = "run",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$executors$setThread(CallbackInfo ci) {
		this.thread = Thread.currentThread();
	}

	@Override
	public void execute(Runnable command) {
		if (this.isOnSameThread()) {
			command.run();
		} else {
			synchronized (this.pendingTasks) {
				this.pendingTasks.add(command);
			}
		}
	}

	@Override
	public boolean isOnSameThread() {
		return Thread.currentThread() == this.thread;
	}

	@Inject(
		method = "tick",
		at  = @At(
			value = "HEAD"
		)
	)
	private void osl$executors$runPendingTasks(CallbackInfo ci) {
		this.runPendingTasks();
	}

	@Unique
	private void runPendingTasks() {
		synchronized (this.pendingTasks) {
			while (!this.pendingTasks.isEmpty()) {
				this.runTask(this.pendingTasks.poll());
			}
		}
	}

	@Unique
	private void runTask(Runnable task) {
		try {
			task.run();
		} catch (Throwable t) {
			Executors.LOGGER.fatal("Error running task", t);
		}
	}
}
