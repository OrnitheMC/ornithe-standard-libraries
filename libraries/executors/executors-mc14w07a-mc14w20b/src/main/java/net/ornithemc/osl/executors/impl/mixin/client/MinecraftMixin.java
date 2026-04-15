package net.ornithemc.osl.executors.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.executors.api.MainThreadExecutor;
import net.ornithemc.osl.executors.impl.Executors;

@Mixin(Minecraft.class)
public class MinecraftMixin implements MainThreadExecutor {

	@Shadow
	private ListenableFuture<?> executeTask(Runnable task) { return null; }

	@Shadow
	private boolean isOnSameThread() { return false; }

	@Override
	public void execute(Runnable command) {
		this.executeTask(command);
	}

	@Override
	public boolean isRunningOnSameThread() {
		return this.isOnSameThread();
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
