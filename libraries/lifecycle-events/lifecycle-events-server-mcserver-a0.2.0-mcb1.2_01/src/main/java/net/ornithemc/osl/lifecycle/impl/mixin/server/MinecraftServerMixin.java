package net.ornithemc.osl.lifecycle.impl.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.lifecycle.api.MinecraftEvents;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Unique private boolean osl$lifecycle$stopped;

	@Inject(
		method = "run",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$start(CallbackInfo ci) {
		MinecraftEvents.START.invoker().accept((MinecraftServer)(Object)this);
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
		MinecraftEvents.READY.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "stop",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$stop(CallbackInfo ci) {
		if (!osl$lifecycle$stopped) {
			osl$lifecycle$stopped = true;
			MinecraftEvents.STOP.invoker().accept((MinecraftServer)(Object)this);
		}
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		MinecraftEvents.TICK_START.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfo ci) {
		MinecraftEvents.TICK_END.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "loadWorld",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$loadWorld(CallbackInfo ci) {
		MinecraftEvents.LOAD_WORLD.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "loadWorld",
		at = @At(
			value = "INVOKE",
			shift = Shift.AFTER,
			target = "Lnet/minecraft/server/PlayerManager;setWorld(Lnet/minecraft/server/world/ServerWorld;)V"
		)
	)
	private void osl$lifecycle$prepareWorld(CallbackInfo ci) {
		MinecraftEvents.PREPARE_WORLD.invoker().accept((MinecraftServer)(Object)this);
	}

	@Inject(
		method = "loadWorld",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$readyWorld(CallbackInfo ci) {
		MinecraftEvents.READY_WORLD.invoker().accept((MinecraftServer)(Object)this);
	}
}
