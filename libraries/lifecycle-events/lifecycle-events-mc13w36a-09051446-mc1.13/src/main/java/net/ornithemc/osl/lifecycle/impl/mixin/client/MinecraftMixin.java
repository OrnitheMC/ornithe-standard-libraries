package net.ornithemc.osl.lifecycle.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.impl.client.MinecraftAccess;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$start(CallbackInfo ci) {
		MinecraftAccess.INSTANCE = (Minecraft)(Object)this;
		MinecraftClientEvents.START.invoker().accept(MinecraftAccess.INSTANCE);
	}

	@Inject(
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$ready(CallbackInfo ci) {
		MinecraftClientEvents.READY.invoker().accept(MinecraftAccess.INSTANCE);
	}

	@Inject(
		method = "shutdown",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$stop(CallbackInfo ci) {
		MinecraftClientEvents.STOP.invoker().accept(MinecraftAccess.INSTANCE);
	}

	@Inject(
		method = "shutdown",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$stopped(CallbackInfo ci) {
		MinecraftAccess.INSTANCE = null;
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		MinecraftClientEvents.TICK_START.invoker().accept(MinecraftAccess.INSTANCE);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfo ci) {
		MinecraftClientEvents.TICK_END.invoker().accept(MinecraftAccess.INSTANCE);
	}
}
