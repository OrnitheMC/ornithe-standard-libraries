package net.ornithemc.osl.enchantments.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.osl.enchantments.impl.EnchantmentRegistryImpl;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

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
}
