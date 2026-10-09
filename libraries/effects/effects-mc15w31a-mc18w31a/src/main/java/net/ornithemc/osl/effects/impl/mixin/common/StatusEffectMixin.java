package net.ornithemc.osl.effects.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.living.effect.StatusEffect;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.effects.impl.StatusEffectRegistryImpl;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {

	@Conditional(minecraftVersion = @Version("<1.13-pre5"))
	@Shadow
	private String key;

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

	@Conditional(minecraftVersion = @Version("<1.13-pre5"))
	@Inject(
		method = "getTranslationKey",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$effects$autoAssignTranslationKey(CallbackInfoReturnable<String> cir) {
		if (this.key == null) {
			NamespacedIdentifier identifier = StatusEffectRegistryImpl.getIdentifier((StatusEffect) (Object) this);

			if (identifier == null) {
				this.key = "effect.unknown";
			} else {
				this.key = Util.makeTranslationKey("effect", identifier);
			}
		}
	}
}
