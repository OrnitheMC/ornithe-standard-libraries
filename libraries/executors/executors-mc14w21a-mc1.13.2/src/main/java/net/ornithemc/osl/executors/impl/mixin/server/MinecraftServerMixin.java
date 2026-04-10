package net.ornithemc.osl.executors.impl.mixin.server;

import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockableEventLoop;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements Executor {

	@Override
	public void execute(Runnable command) {
		((BlockableEventLoop) this).executeTask(command);
	}
}
