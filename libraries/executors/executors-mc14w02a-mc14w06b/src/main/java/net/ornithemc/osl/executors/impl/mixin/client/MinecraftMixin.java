package net.ornithemc.osl.executors.impl.mixin.client;

import java.util.ArrayDeque;
import java.util.Queue;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.util.profiler.Profiler;

import net.ornithemc.osl.executors.api.MainThreadExecutor;
import net.ornithemc.osl.executors.impl.Executors;

@Mixin(Minecraft.class)
public class MinecraftMixin implements MainThreadExecutor {

	@Shadow @Final
	private Profiler profiler;

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
		if (this.isRunningOnSameThread()) {
			command.run();
		} else {
			synchronized (this.pendingTasks) {
				this.pendingTasks.add(command);
			}
		}
	}

	@Override
	public boolean isRunningOnSameThread() {
		return Thread.currentThread() == this.thread;
	}

	@Inject(
		method = "runGame",
		at  = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/TickTimer;advance()V",
			shift = Shift.AFTER
		)
	)
	private void osl$executors$runPendingTasks(CallbackInfo ci) {
		this.profiler.push("scheduledExecutables");
		this.runPendingTasks();
		this.profiler.pop();
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

	@Inject(
		method = "shutdown",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$executors$shutdownBackgroundExecutor(CallbackInfo ci) {
		Executors.shutdownBackgroundExecutor();
	}
}
