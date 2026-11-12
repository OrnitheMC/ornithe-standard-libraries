package net.ornithemc.osl.blocks.impl;

import net.ornithemc.osl.registries.api.RegistryEvents;

public final class BlocksInit {

	public static void init() {
		RegistryEvents.INIT_REGISTRIES.register(BlockRegistryImpl::init);
	}
}
