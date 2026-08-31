package net.ornithemc.osl.biomes.impl;

import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.IdRegistry;
import net.minecraft.world.biome.Biome;

/**
 * The biome registry is created and populated in Biome's class initializer.
 * This breaks OSL's usual pattern of registering the registry in the API entrypoint but
 * triggering registry population during bootstrapping, since OSL has to wrap the Vanilla
 * registry which is not possible without triggering Biome class load and registry
 * population.
 * The solution: move the Vanilla registry to another class and replace the IdRegistry
 * reference in Biome with this one.
 */
public final class BiomeIdRegistry {

	public static final IdRegistry<Identifier, Biome> REGISTRY = new IdRegistry<>();

}
