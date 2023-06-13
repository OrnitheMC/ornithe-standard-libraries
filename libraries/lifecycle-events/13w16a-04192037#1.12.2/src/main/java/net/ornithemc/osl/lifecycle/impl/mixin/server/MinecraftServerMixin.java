package net.ornithemc.osl.lifecycle.impl.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(
		method = "main",
		at = @At(
			value = "NEW",
			target = "Lnet/minecraft/server/dedicated/DedicatedServer;<init>()V"
		)
	)
	private void osl$lifecycle$parseRunArgs(String[] args, CallbackInfo ci) {
		MinecraftServerEvents.PRE_START.invoker().accept(args);
	}
}
