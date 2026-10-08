package net.ornithemc.osl.enchantments.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.enchantments.impl.EnchantmentRegistryImpl;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

	@Conditional(minecraftVersion = @Version("<1.13-pre5"))
	@Shadow
	private String key;

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$enchantments$unlockEnchantmentRegistry(CallbackInfo ci) {
		EnchantmentRegistryImpl.unlock();
	}

	@Inject(
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$enchantments$registerEnchantments(CallbackInfo ci) {
		EnchantmentRegistryImpl.registerEnchantments();
	}

	@Conditional(minecraftVersion = @Version("<1.13-pre5"))
	@Inject(
		method = "getTranslationKey",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$enchantments$autoAssignTranslationKey(CallbackInfoReturnable<String> cir) {
		if (this.key == null) {
			NamespacedIdentifier identifier = EnchantmentRegistryImpl.getIdentifier((Enchantment) (Object) this);

			if (identifier == null) {
				this.key = "unknown";
			} else {
				this.key = Util.makeTranslationKey(identifier);
			}
		}
	}
}
