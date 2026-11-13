package net.ornithemc.osl.items.impl;

import net.ornithemc.osl.registries.api.RegistryEvents;

public final class ItemsInit {

	public static void init() {
		RegistryEvents.INIT_REGISTRIES.register(ItemRegistryImpl::init);
	}
}
