package net.ornithemc.osl.executors.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockableEventLoop;

import net.ornithemc.osl.executors.api.MainThreadExecutor;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MainThreadExecutor {

	@Override
	public void execute(Runnable command) {
		((BlockableEventLoop) this).executeTask(command);
	}

	@Override
	public boolean isRunningOnSameThread() {
		return ((BlockableEventLoop) this).isOnSameThread();
	}
}
