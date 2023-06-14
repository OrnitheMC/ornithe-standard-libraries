package net.ornithemc.osl.lifecycle.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.world.ClientWorld;

import net.ornithemc.osl.lifecycle.api.client.ClientWorldEvents;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		ClientWorldEvents.TICK_START.invoker().accept((ClientWorld)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfo ci) {
		ClientWorldEvents.TICK_END.invoker().accept((ClientWorld)(Object)this);
	}
}
