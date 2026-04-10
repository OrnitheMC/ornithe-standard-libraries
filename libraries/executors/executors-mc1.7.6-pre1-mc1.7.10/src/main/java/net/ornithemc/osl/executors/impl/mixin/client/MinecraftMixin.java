package net.ornithemc.osl.executors.impl.mixin.client;

import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MinecraftMixin implements Executor {

	@Shadow
	private ListenableFuture<?> executeTask(Runnable task) { return null; }

	@Override
	public void execute(Runnable command) {
		this.executeTask(command);
	}
}
