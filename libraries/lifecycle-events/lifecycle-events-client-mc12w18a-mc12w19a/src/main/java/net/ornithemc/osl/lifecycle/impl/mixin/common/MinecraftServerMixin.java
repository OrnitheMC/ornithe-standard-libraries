package net.ornithemc.osl.lifecycle.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Shadow private boolean stopped;

	@Inject(
		method = "run",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$start(CallbackInfo ci) {
		MinecraftServerEvents.START.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "run",
		at = @At(
			value = "INVOKE",
			shift = Shift.AFTER,
			target = "Lnet/minecraft/server/MinecraftServer;init()Z"
		)
	)
	private void osl$lifecycle$ready(CallbackInfo ci) {
		MinecraftServerEvents.READY.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "stop",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$stop(CallbackInfo ci) {
		if (!stopped) {
			MinecraftServerEvents.STOP.invoker().accept((MinecraftServer)(Object)this);
		}
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		MinecraftServerEvents.TICK_START.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfo ci) {
		MinecraftServerEvents.TICK_END.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "loadWorld",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$loadWorld(CallbackInfo ci) {
		MinecraftServerEvents.LOAD_WORLD.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "loadWorld",
		at = @At(
			value = "INVOKE",
			remap = false,
			ordinal = 0,
			target = "Ljava/lang/System;currentTimeMillis()J"
		)
	)
	private void osl$lifecycle$prepareWorld(CallbackInfo ci) {
		MinecraftServerEvents.PREPARE_WORLD.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "loadWorld",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$readyWorld(CallbackInfo ci) {
		MinecraftServerEvents.READY_WORLD.invoker().accept((MinecraftServer)(Object)this);
	}
}
