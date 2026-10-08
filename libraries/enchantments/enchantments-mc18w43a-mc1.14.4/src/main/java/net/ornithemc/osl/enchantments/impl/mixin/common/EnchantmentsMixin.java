package net.ornithemc.osl.enchantments.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.Enchantments;

import net.ornithemc.osl.enchantments.impl.EnchantmentRegistryImpl;

@Mixin(Enchantments.class)
public class EnchantmentsMixin {

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$enchantments$unlockEnchantmentRegistry(CallbackInfo ci) {
		EnchantmentRegistryImpl.unlock();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$enchantments$registerEnchantments(CallbackInfo ci) {
		EnchantmentRegistryImpl.registerEnchantments();
	}
}
