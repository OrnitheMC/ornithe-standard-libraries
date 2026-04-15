package net.ornithemc.osl.executors.impl.mixin.client;

import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockableEventLoop;

import net.ornithemc.osl.executors.impl.Executors;

@Mixin(Minecraft.class)
public class MinecraftMixin implements Executor {

	@Override
	public void execute(Runnable command) {
		((BlockableEventLoop) this).executeTask(command);
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
