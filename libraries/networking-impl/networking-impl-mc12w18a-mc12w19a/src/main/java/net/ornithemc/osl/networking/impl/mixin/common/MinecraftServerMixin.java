package net.ornithemc.osl.networking.impl.mixin.common;

import java.util.ArrayDeque;
import java.util.Queue;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.networking.impl.access.TaskRunnerAccess;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements TaskRunnerAccess {

	@Unique
	private Queue<Runnable> tasks = new ArrayDeque<>();

	@Inject(
		method = "tick",
		at = @At(
			value = "FIELD",
			opcode = Opcodes.PUTFIELD,
			target = "Lnet/minecraft/server/MinecraftServer;ticks:I"
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
