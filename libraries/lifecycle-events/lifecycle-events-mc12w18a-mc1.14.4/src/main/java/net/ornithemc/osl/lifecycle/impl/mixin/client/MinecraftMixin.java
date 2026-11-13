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

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.impl.client.MinecraftAccess;

@Mixin(Minecraft.class)
public class MinecraftMixin {

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
		MinecraftAccess.INSTANCE = (Minecraft)(Object)this;
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
		MinecraftAccess.INSTANCE = (Minecraft)(Object)this;
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
}
