package net.ornithemc.osl.biomes.api.biome;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface BiomeExtension {

	interface BuilderExtension {

		Biome.Builder parent(NamespacedIdentifier identifier);

	}
}
