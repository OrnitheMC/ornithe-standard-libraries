package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockableEventLoop;

import net.ornithemc.osl.networking.impl.access.TaskRunnerAccess;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends BlockableEventLoop implements TaskRunnerAccess {

	private MinecraftServerMixin(String name) {
		super(name);
	}

	@Override
	public boolean osl$networking$submit(Runnable task) {
		this.execute(task);
		return true;
	}
}
