package net.ornithemc.osl.biomes.impl.biome;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.api.biome.BiomeExtension;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface BiomeExtensionImpl extends BiomeExtension {

	interface BuilderExtensionImpl extends BuilderExtension {

		@Override
		default Biome.Builder parent(NamespacedIdentifier identifier) {
			throw new AbstractMethodError();
		}
	}
}
