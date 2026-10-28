package net.ornithemc.osl.lifecycle.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.lifecycle.impl.server.MinecraftServerAccess;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Unique
	private boolean stopped;

	@Conditional(minecraftVersion = @Version("<18w43a"))
	@Inject(
		method = "<init>*",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Object;<init>()V",
			shift = Shift.AFTER
		)
	)
	private void osl$lifecycle$init1(CallbackInfo ci) {
		MinecraftServerAccess.INSTANCE = (MinecraftServer)(Object)this;
	}

	@Conditional(minecraftVersion = @Version(">=18w43a <19w11a"))
	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/BlockableEventLoop;<init>()V",
			shift = Shift.AFTER
		)
	)
	private void osl$lifecycle$init2(CallbackInfo ci) {
		MinecraftServerAccess.INSTANCE = (MinecraftServer)(Object)this;
	}

	@Conditional(minecraftVersion = @Version(">=19w11a"))
	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/unmapped/C_08114195;<init>(Ljava/lang/String;)V",
			shift = Shift.AFTER
		)
	)
	private void osl$lifecycle$init3(CallbackInfo ci) {
		MinecraftServerAccess.INSTANCE = (MinecraftServer)(Object)this;
	}

	@Inject(
		method = "run",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$start(CallbackInfo ci) {
		MinecraftServerEvents.START.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Inject(
		method = "run",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/MinecraftServer;init()Z",
			shift = Shift.AFTER
		)
	)
	private void osl$lifecycle$ready(CallbackInfo ci) {
		MinecraftServerEvents.READY.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@WrapMethod(method = "shutdown")
	private void osl$lifecycle$stop(Operation<Void> op) {
		boolean triggerEvents = !this.stopped && MinecraftServerAccess.INSTANCE != null;

		if (triggerEvents) {
			this.stopped = true;
			MinecraftServerEvents.STOP.invoker().accept(MinecraftServerAccess.INSTANCE);
		}

		op.call();

		if (triggerEvents) {
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

	@Conditional(minecraftVersion = @Version("<12w21a"))
	@Inject(
		method = "loadWorld",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$loadWorld1(CallbackInfo ci) {
		MinecraftServerEvents.LOAD_WORLD.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Conditional(minecraftVersion = @Version("<12w21a"))
	@Inject(
		method = "loadWorld",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/System;currentTimeMillis()J",
			ordinal = 0,
			remap = false
		)
	)
	private void osl$lifecycle$prepareWorld1(CallbackInfo ci) {
		MinecraftServerEvents.PREPARE_WORLD.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Conditional(minecraftVersion = @Version("<12w21a"))
	@Inject(
		method = "loadWorld",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$readyWorld1(CallbackInfo ci) {
		MinecraftServerEvents.READY_WORLD.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Conditional(minecraftVersion = @Version(">=12w21a"))
	@Inject(
		method = "convertWorld",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$loadWorld2(CallbackInfo ci) {
		MinecraftServerEvents.LOAD_WORLD.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Conditional(minecraftVersion = @Version(">=12w21a"))
	@Inject(
		method = "prepareWorlds",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$prepareWorld2(CallbackInfo ci) {
		MinecraftServerEvents.PREPARE_WORLD.invoker().accept(MinecraftServerAccess.INSTANCE);
	}

	@Conditional(minecraftVersion = @Version(">=12w21a"))
	@Inject(
		method = "prepareWorlds",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$readyWorld2(CallbackInfo ci) {
		MinecraftServerEvents.READY_WORLD.invoker().accept(MinecraftServerAccess.INSTANCE);
	}
}
