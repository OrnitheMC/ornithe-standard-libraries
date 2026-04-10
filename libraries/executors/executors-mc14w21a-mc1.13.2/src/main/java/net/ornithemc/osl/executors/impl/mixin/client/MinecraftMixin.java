package net.ornithemc.osl.executors.impl.mixin.client;

import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockableEventLoop;

@Mixin(Minecraft.class)
public class MinecraftMixin implements Executor {

	@Override
	public void execute(Runnable command) {
		((BlockableEventLoop) this).executeTask(command);
	}
}
