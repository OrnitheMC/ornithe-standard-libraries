package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockableEventLoop;

import net.ornithemc.osl.networking.impl.access.TaskRunnerAccess;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements BlockableEventLoop, TaskRunnerAccess {

	@Override
	public boolean osl$networking$submit(Runnable task) {
		this.submit(task);
		return true;
	}
}
