package net.ornithemc.osl.executors.impl.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.executors.impl.Executors;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

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
