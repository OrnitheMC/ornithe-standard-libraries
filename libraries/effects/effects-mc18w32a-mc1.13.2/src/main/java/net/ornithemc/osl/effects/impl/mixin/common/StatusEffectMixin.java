package net.ornithemc.osl.effects.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.living.effect.StatusEffect;

import net.ornithemc.osl.effects.impl.StatusEffectRegistryImpl;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$effects$unlockStatusEffectRegistry(CallbackInfo ci) {
		StatusEffectRegistryImpl.unlock();
	}

	@Inject(
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$effects$registerStatusEffects(CallbackInfo ci) {
		StatusEffectRegistryImpl.registerStatusEffects();
	}
}
