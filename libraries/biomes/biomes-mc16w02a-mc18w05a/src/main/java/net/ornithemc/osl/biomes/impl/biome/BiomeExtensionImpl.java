package net.ornithemc.osl.biomes.impl.biome;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.api.biome.BiomeExtension;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface BiomeExtensionImpl extends BiomeExtension {

	interface SettingsExtensionImpl extends SettingsExtension {

		@Override
		default Biome.Settings parent(NamespacedIdentifier identifier) {
			throw new AbstractMethodError();
		}
	}
}
