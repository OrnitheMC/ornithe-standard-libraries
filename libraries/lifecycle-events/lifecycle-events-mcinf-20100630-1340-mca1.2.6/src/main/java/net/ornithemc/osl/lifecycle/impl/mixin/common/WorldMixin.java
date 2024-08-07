package net.ornithemc.osl.lifecycle.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;

import net.ornithemc.osl.lifecycle.api.WorldEvents;
import net.ornithemc.osl.lifecycle.impl.client.MinecraftAccess;

@Mixin(World.class)
public class WorldMixin {

	/*
	 * the isMultiplayer field was added in a1.0.11 so we check
	 * against the Minecraft.isMultiplayer method instead
	 */

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		if (!MinecraftAccess.INSTANCE.isMultiplayer()) {
			WorldEvents.TICK_START.invoker().accept((World)(Object)this);
		}
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfo ci) {
		if (!MinecraftAccess.INSTANCE.isMultiplayer()) {
			WorldEvents.TICK_END.invoker().accept((World)(Object)this);
		}
	}
}
