package net.ornithemc.osl.blockentities.impl;

import net.ornithemc.osl.registries.api.RegistryEvents;

public final class BlockEntitiesInit {

	public static void init() {
		RegistryEvents.INIT_REGISTRIES.register(BlockEntityTypeRegistryImpl::init);
	}
}
