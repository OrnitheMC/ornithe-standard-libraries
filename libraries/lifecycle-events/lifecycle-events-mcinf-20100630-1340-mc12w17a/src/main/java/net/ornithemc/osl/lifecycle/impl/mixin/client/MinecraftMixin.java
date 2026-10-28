package net.ornithemc.osl.lifecycle.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.impl.client.MinecraftAccess;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Unique
	private boolean stopped;
	@Unique
	private int startGameDepth;

	@Inject(
		method = "<init>*",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Object;<init>()V",
			shift = Shift.AFTER
		)
	)
	private void osl$lifecycle$init1(CallbackInfo ci) {
		MinecraftAccess.INSTANCE = (Minecraft)(Object)this;
	}

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$start(CallbackInfo ci) {
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

	@WrapMethod(method = "shutdown")
	private void osl$lifecycle$stop(Operation<Void> op) {
		boolean triggerEvents = !this.stopped && MinecraftAccess.INSTANCE != null;

		if (triggerEvents) {
			this.stopped = true;
			MinecraftClientEvents.STOP.invoker().accept(MinecraftAccess.INSTANCE);
		}

		op.call();

		if (triggerEvents) {
			MinecraftAccess.INSTANCE = null;
		}
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

	@Inject(
		method = "startGame",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$loadWorld(CallbackInfo ci) {
		// The startGame method recursively calls itself when converting from
		// older world formats, but we only want to capture the initial call.
		if (this.startGameDepth++ == 0) {
			MinecraftClientEvents.LOAD_WORLD.invoker().accept(MinecraftAccess.INSTANCE);
		}
	}

	@Inject(
		method = "startGame",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/World;isNew:Z"
		)
	)
	private void osl$lifecycle$prepareWorld(CallbackInfo ci) {
		MinecraftClientEvents.PREPARE_WORLD.invoker().accept(MinecraftAccess.INSTANCE);
	}

	@Inject(
		method = "startGame",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$readyWorld(CallbackInfo ci) {
		// The startGame method recursively calls itself when converting from
		// older world formats, but we only want to capture the initial call.
		if (--this.startGameDepth == 0) {
			MinecraftClientEvents.READY_WORLD.invoker().accept(MinecraftAccess.INSTANCE);
		}
	}
}
