package net.ornithemc.osl.biomes.impl;

import java.util.Set;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.biomes.api.BiomeEvents;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

public final class BiomeRegistryImpl {

	public static final Registry<Biome> REGISTRY = VanillaRegistries.registerSimple(RegistryKeys.BIOME, net.minecraft.util.registry.Registry.BIOME);

	private static boolean locked = true;

	public static int getId(Biome biome) {
		return REGISTRY.getId(biome);
	}

	public static NamespacedIdentifier getIdentifier(Biome biome) {
		return REGISTRY.getIdentifier(biome);
	}

	public static ResourceKey<Biome> getKey(Biome biome) {
		return REGISTRY.getKey(biome);
	}

	public static Biome getBiome(int id) {
		return REGISTRY.get(id);
	}

	public static Biome getBiome(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Biome getBiome(ResourceKey<Biome> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Biome>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends Biome> T register(NamespacedIdentifier identifier, T biome) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (biome.isMutated()) {
				registerBiomeMutation(identifier, biome);
			}

			return Registry.register(REGISTRY, identifier, biome);
		}
	}

	public static <T extends Biome> T register(ResourceKey<Biome> key, T biome) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (biome.isMutated()) {
				registerBiomeMutation(key.identifier(), biome);
			}

			return Registry.register(REGISTRY, key, biome);
		}
	}

	private static void registerBiomeMutation(NamespacedIdentifier identifier, Biome biome) {
		NamespacedIdentifier parentIdentifier = biome.osl$biomes$getParentIdentifier();
		Biome parent = REGISTRY.get(parentIdentifier);

		if (parent == null) {
			throw new IllegalStateException("Error registering mutated biome " + identifier + ": parent " + parentIdentifier + " does not exist!");
		} else {
			Biome.MUTATED_BIOMES.put(biome, REGISTRY.getId(parent));
		}
	}

	public static void init() {
		SyncedRegistries.register(RegistryKeys.BIOME);
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerBiomes() {
		BiomeEvents.REGISTER_BIOMES.invoker().run();
	}
}
