package net.ornithemc.osl.biomes.api.biome;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.impl.BiomeRegistryImpl;
import net.ornithemc.osl.registries.api.registry.Registry;

public interface BiomeExtension {

	Registry<Biome> REGISTRY = BiomeRegistryImpl.REGISTRY;
	int AUTO_ASSIGN_ID = -172;

}
