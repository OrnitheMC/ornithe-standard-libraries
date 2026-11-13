package net.ornithemc.osl.biomes.impl;

import net.ornithemc.osl.registries.api.RegistryEvents;

public final class BiomesInit {

	public static void init() {
		RegistryEvents.INIT_REGISTRIES.register(BiomeRegistryImpl::init);
	}
}
