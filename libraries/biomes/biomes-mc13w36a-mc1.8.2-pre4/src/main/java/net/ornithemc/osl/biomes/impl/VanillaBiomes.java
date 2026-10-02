package net.ornithemc.osl.biomes.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public final class VanillaBiomes {

	/**
	 * Namespaced IDs were introduced in 1.9. Before then, the numerical IDs
	 * were the only unique identifiers for biomes. Here we assign namespaced
	 * IDs to pre-1.9 biomes, matching the 1.9 IDs  where possible.
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
		"jungle_hills",
		"jungle_edge",
		"deep_ocean",
		"stone_beach",
		"cold_beach",
		"birch_forest",
		"birch_forest_hills",
		"roofed_forest",

		"taiga_cold",
		"taiga_cold_hills",
		"redwood_taiga",
		"redwood_taiga_hills",
		"extreme_hills_with_trees",
		"savanna",
		"savanna_rock",
		"mesa",
		"mesa_rock",
		"mesa_clear_rock"
	};
	private static final String[] MUTATED_IDENTIFIERS = {
		null,
		"mutated_plains",
		"mutated_desert",
		"mutated_extreme_hills",
		"mutated_forest",
		"mutated_taiga",
		"mutated_swampland",
		null,
		null,
		null,

		null,
		null,
		"mutated_ice_flats",
		null,
		null,
		null,
		null,
		null,
		null,
		null,

		null,
		"mutated_jungle",
		null,
		"mutated_jungle_edge",
		null,
		null,
		null,
		"mutated_birch_forest",
		"mutated_birch_forest_hills",
		"mutated_roofed_forest",

		"mutated_taiga_cold",
		null,
		"mutated_redwood_taiga",
		"mutated_redwood_taiga_hills",
		"mutated_extreme_hills_with_trees",
		"mutated_savanna",
		"mutated_savanna_rock",
		"mutated_mesa",
		"mutated_mesa_rock",
		"mutated_mesa_clear_rock"
	};

	public static final int MAX_ID = 255;
	public static final int MUTATED_ID_OFFSET = 128;

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

		if (biome.id >= 0 && biome.id < MUTATED_IDENTIFIERS.length) {
			Biome mutated = Biome.BY_ID[biome.id + MUTATED_ID_OFFSET];
			String identifier = MUTATED_IDENTIFIERS[biome.id];

			if (identifier != null) {
				BiomeRegistryImpl.register(NamespacedIdentifiers.from(identifier), mutated);
			}
		}
	}
}
