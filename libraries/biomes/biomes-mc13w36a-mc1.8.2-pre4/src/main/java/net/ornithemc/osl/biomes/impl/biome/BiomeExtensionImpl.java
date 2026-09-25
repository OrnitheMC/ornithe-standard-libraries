package net.ornithemc.osl.biomes.impl.biome;

import net.ornithemc.osl.biomes.api.biome.BiomeExtension;

public interface BiomeExtensionImpl extends BiomeExtension {

	@Override
	default String getName() {
		throw new AbstractMethodError();
	}
}
