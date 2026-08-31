package net.ornithemc.osl.biomes.impl.biome;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

public class BiomeIdFixer implements IdFixer {

	@Override
	public void apply() {
		for (int id = 0; id < Biome.BY_ID.length; id++) {
			Biome biome = Biome.BY_ID[id];

			if (biome != null) {
				biome.id = id;
			}
		}
	}
}
