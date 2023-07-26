package net.ornithemc.osl.lifecycle.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.lifecycle.api.MinecraftEvents;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$start(CallbackInfo ci) {
		MinecraftEvents.START.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$ready(CallbackInfo ci) {
		MinecraftEvents.READY.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "stop",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$stop(CallbackInfo ci) {
		MinecraftEvents.STOP.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		MinecraftEvents.TICK_START.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfo ci) {
		MinecraftEvents.TICK_END.invoker().accept((Minecraft)(Object)this);
	}

	@Unique private int osl$lifecycle$startGameDepth;

	@Inject(
		method = "startGame",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$loadWorld(CallbackInfo ci) {
		if (osl$lifecycle$startGameDepth++ == 0) {
			// The startGame method recursively calls itself when converting from
			// older world formats, but we only want to capture the initial call.
			MinecraftEvents.LOAD_WORLD.invoker().accept((Minecraft)(Object)this);
		}
	}

	@Inject(
		method = "startGame",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/World;f_5995036:Z"
		)
	)
	private void osl$lifecycle$prepareWorld(CallbackInfo ci) {
		MinecraftEvents.PREPARE_WORLD.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "startGame",
		at = @At(
			value = "RETURN"
		)
	)
	private void osl$lifecycle$readyWorld(CallbackInfo ci) {
		if (--osl$lifecycle$startGameDepth == 0) {
			// The startGame method recursively calls itself when converting from
			// older world formats, but we only want to capture the initial call.
			MinecraftEvents.READY_WORLD.invoker().accept((Minecraft)(Object)this);
		}
	}
}
