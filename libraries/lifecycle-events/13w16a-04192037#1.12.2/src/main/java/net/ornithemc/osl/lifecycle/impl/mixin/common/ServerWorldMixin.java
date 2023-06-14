package net.ornithemc.osl.lifecycle.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import net.ornithemc.osl.lifecycle.api.server.ServerWorldEvents;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$load(CallbackInfoReturnable<World> cir) {
		ServerWorldEvents.LOAD.invoker().accept((ServerWorld)(Object)this);
	}

	@Inject(
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$ready(CallbackInfoReturnable<World> cir) {
		ServerWorldEvents.READY.invoker().accept((ServerWorld)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		ServerWorldEvents.TICK_START.invoker().accept((ServerWorld)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfo ci) {
		ServerWorldEvents.TICK_END.invoker().accept((ServerWorld)(Object)this);
	}
}
