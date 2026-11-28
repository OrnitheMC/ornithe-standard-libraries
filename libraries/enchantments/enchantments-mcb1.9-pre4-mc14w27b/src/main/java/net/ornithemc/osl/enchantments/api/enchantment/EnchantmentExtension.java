package net.ornithemc.osl.enchantments.api.enchantment;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.osl.enchantments.impl.EnchantmentRegistryImpl;
import net.ornithemc.osl.registries.api.registry.Registry;

public interface EnchantmentExtension {

	Registry<Enchantment> REGISTRY = EnchantmentRegistryImpl.REGISTRY;
	int AUTO_ASSIGN_ID = -172;

}
