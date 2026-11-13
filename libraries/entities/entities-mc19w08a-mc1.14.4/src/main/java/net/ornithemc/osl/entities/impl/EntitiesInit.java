package net.ornithemc.osl.entities.impl;

import net.ornithemc.osl.registries.api.RegistryEvents;

public final class EntitiesInit {

	public static void init() {
		RegistryEvents.INIT_REGISTRIES.register(EntityTypeRegistryImpl::init);
	}
}
