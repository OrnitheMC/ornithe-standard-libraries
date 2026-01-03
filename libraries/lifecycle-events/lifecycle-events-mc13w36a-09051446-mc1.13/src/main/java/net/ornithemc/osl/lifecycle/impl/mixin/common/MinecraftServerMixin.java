package net.ornithemc.osl.lifecycle.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.lifecycle.impl.server.MinecraftServerAccess;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Unique private int osl$lifecycle$shutdownDepth;

	@Inject(
		method = "run",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$start(CallbackInfo ci) {
		MinecraftServerAccess.INSTANCE = (MinecraftServer)(Object)this;
		MinecraftServerEvents.START.invoker().accept(MinecraftServerAccess.INSTANCE);
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
		MinecraftServerEvents.READY.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Inject(
		method = "shutdown",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$stop(CallbackInfo ci) {
		if (osl$lifecycle$shutdownDepth++ == 0) {
			MinecraftServerEvents.STOP.invoker().accept(MinecraftServerAccess.INSTANCE);
		}
	}

	@Inject(
		method = "shutdown",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$stopped(CallbackInfo ci) {
		if (--osl$lifecycle$shutdownDepth == 0) {
			MinecraftServerAccess.INSTANCE = null;
		}
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		MinecraftServerEvents.TICK_START.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfo ci) {
		MinecraftServerEvents.TICK_END.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Inject(
		method = "convertWorld",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$loadWorld(CallbackInfo ci) {
		MinecraftServerEvents.LOAD_WORLD.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Inject(
		method = "prepareWorlds",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$prepareWorld(CallbackInfo ci) {
		MinecraftServerEvents.PREPARE_WORLD.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Inject(
		method = "prepareWorlds",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$readyWorld(CallbackInfo ci) {
		MinecraftServerEvents.READY_WORLD.invoker().accept(MinecraftServerAccess.INSTANCE);
	}
}
