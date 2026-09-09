package net.ornithemc.osl.biomes.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public final class VanillaBiomes {

	/**
	 * Namespaced IDs were introduced in 1.9. Before then, the numerical IDs
	 * were the only unique identifiers for biomes. Here we assign namespaced
	 * IDs to pre-1.7 biomes, matching the 1.9 IDs  where possible.
	 */
	private static final String[] IDENTIFIERS = {
		// grouped per 10 for easier lookup

		"ocean",
		"plains",
		"desert",
		"extreme_hills",
		"forest",
		"taiga",
		"swampland",
		"river",
		"hell",
		"sky",

		"frozen_ocean",
		"frozen_river",
		"ice_flats",
		"ice_mountains",
		"mushroom_island",
		"mushroom_island_shore",
		"beaches",
		"desert_hills",
		"forest_hills",
		"taiga_hills",

		"smaller_extreme_hills",
		"jungle",
		"jungle_hills"
	};

	public static final int MAX_ID = 255;

	static void init() {
		for (Field f : Biome.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers()) && Biome.class.isAssignableFrom(f.getType())) {
				try {
					Biome biome = (Biome) f.get(null);

					if (biome != null) {
						register(biome);
					}
				} catch (Throwable t) {
				}
			}
		}
	}

	private static void register(Biome biome) {
		if (biome.id >= 0 && biome.id < IDENTIFIERS.length) {
			String identifier = IDENTIFIERS[biome.id];

			if (identifier != null) {
				BiomeRegistryImpl.register(NamespacedIdentifiers.from(identifier), biome);
			}
		}
	}
}
