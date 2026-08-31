package net.ornithemc.osl.biomes.api.biome;

import net.minecraft.world.biome.Biome;

/**
 * Utilities for constructing and modifying biome settings.
 */
public final class BiomeSettings {

	/**
	 * Constructs a new biome settings builder without a custom display name.
	 * Instead, a translation key is generated once the biome is registered.
	 * 
	 * @return a new biome settings builder with a localized biome name.
	 */
	public static Biome.Settings builder() {
		return new Biome.Settings(null);
	}
}
