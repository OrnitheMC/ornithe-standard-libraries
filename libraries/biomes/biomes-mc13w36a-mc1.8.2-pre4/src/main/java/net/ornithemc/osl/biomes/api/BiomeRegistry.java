package net.ornithemc.osl.biomes.api;

import java.util.Set;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.biomes.impl.BiomeRegistryImpl;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

/**
 * Public access to the Biomes registry.
 */
public final class BiomeRegistry {

	/**
	 * @return the numerical ID assigned to the given biome.
	 */
	public static int getId(Biome biome) {
		return BiomeRegistryImpl.getId(biome);
	}

	/**
	 * @return the namespaced ID assigned to the given biome.
	 */
	public static NamespacedIdentifier getIdentifier(Biome biome) {
		return BiomeRegistryImpl.getIdentifier(biome);
	}

	/**
	 * @return the resource key assigned to the given biome.
	 */
	public static ResourceKey<Biome> getKey(Biome biome) {
		return BiomeRegistryImpl.getKey(biome);
	}

	/**
	 * @return the biome mapped to the given numerical ID.
	 */
	public static Biome getBiome(int id) {
		return BiomeRegistryImpl.getBiome(id);
	}

	/**
	 * @return the biome mapped to the given namespaced ID.
	 */
	public static Biome getBiome(NamespacedIdentifier identifier) {
		return BiomeRegistryImpl.getBiome(identifier);
	}

	/**
	 * @return the biome mapped to the given resource key.
	 */
	public static Biome getBiome(ResourceKey<Biome> key) {
		return BiomeRegistryImpl.getBiome(key);
	}

	/**
	 * @return a set containing all namespaced IDs in the registry.
	 */
	public static Set<NamespacedIdentifier> identifierSet() {
		return BiomeRegistryImpl.identifierSet();
	}

	/**
	 * @return a set containing all resource keys in the registry.
	 */
	public static Set<ResourceKey<Biome>> keySet() {
		return BiomeRegistryImpl.keySet();
	}

	/**
	 * @param <T>        the biome type.
	 * @param identifier the namespaced ID of the biome.
	 * @param biome       the biome to register.
	 * @return the registered biome.
	 */
	public static <T extends Biome> T register(NamespacedIdentifier identifier, T biome) {
		return BiomeRegistryImpl.register(identifier, biome);
	}

	/**
	 * @param <T>   the biome type.
	 * @param key   the resource key of the biome.
	 * @param biome  the biome to register.
	 * @return the registered biome.
	 */
	public static <T extends Biome> T register(ResourceKey<Biome> key, T biome) {
		return BiomeRegistryImpl.register(key, biome);
	}

	/**
	 * @param <T>   the biome type.
	 * @param id    the numerical ID of the biome.
	 * @param key   the namespaced ID of the biome.
	 * @param biome  the biome to register.
	 * @return the registered biome.
	 * 
	 * @deprecated use {@linkplain #register(NamespacedIdentifier, Biome)}
	 *             or {@linkplain #register(ResourceKey, Biome)} instead.
	 */
	@Deprecated
	public static <T extends Biome> T register(int id, NamespacedIdentifier key, T biome) {
		return BiomeRegistryImpl.register(id, key, biome);
	}
}
