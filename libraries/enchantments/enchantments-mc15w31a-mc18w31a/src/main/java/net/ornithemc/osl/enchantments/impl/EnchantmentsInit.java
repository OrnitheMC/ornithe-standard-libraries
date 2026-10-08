package net.ornithemc.osl.enchantments.impl;

import net.ornithemc.osl.registries.api.RegistryEvents;

public final class EnchantmentsInit {

	public static void init() {
		RegistryEvents.INIT_REGISTRIES.register(EnchantmentRegistryImpl::init);
	}
}
