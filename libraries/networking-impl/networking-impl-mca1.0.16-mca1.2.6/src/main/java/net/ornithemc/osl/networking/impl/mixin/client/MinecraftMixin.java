package net.ornithemc.osl.networking.impl.mixin.client;

import java.util.ArrayDeque;
import java.util.Queue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.networking.impl.access.TaskRunnerAccess;

@Mixin(Minecraft.class)
public class MinecraftMixin implements TaskRunnerAccess {

	@Unique
	private Queue<Runnable> tasks = new ArrayDeque<>();

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$runTasks(CallbackInfo ci) {
		synchronized (this.tasks) {
			while (!this.tasks.isEmpty()) {
				this.tasks.poll().run();
			}
		}
	}

	@Override
	public boolean osl$networking$submit(Runnable task) {
		synchronized (this.tasks) {
			this.tasks.add(task);
		}

		return true;
	}
}
