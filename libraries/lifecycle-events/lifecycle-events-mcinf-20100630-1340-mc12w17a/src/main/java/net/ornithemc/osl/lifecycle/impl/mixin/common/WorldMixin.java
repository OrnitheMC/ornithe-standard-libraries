package net.ornithemc.osl.lifecycle.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.GameSide;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.lifecycle.api.WorldEvents;

@Conditional(minecraftVersion = @Version(value = ">=a1.0.5", gameSide = GameSide.CLIENT))
@Mixin(World.class)
public class WorldMixin {

	@Shadow
	private boolean isMultiplayer;

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		if (!this.isMultiplayer) {
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
		if (!this.isMultiplayer) {
			WorldEvents.TICK_END.invoker().accept((World)(Object)this);
		}
	}
}
