package net.ornithemc.osl.lifecycle.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

import net.ornithemc.osl.lifecycle.api.client.ClientWorldEvents;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfoReturnable<World> cir) {
		ClientWorldEvents.TICK_START.invoker().accept((ClientWorld)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfoReturnable<World> cir) {
		ClientWorldEvents.TICK_END.invoker().accept((ClientWorld)(Object)this);
	}
}
