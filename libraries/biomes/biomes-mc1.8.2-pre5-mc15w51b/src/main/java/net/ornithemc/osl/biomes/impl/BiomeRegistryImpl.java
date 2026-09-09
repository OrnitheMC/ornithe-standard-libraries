package net.ornithemc.osl.biomes.impl;

import java.util.Set;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.biomes.api.BiomeEvents;
import net.ornithemc.osl.biomes.impl.biome.BiomeIdFixer;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public final class BiomeRegistryImpl {

	public static final Registry<Biome> REGISTRY = Registries.registerSimple(RegistryKeys.BIOME, () -> Biome.BY_ID.getClass());

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

	@SuppressWarnings("deprecation")
	public static <T extends Biome> T register(NamespacedIdentifier identifier, T biome) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, biome.id, identifier, biome);
		}
	}

	@SuppressWarnings("deprecation")
	public static <T extends Biome> T register(ResourceKey<Biome> key, T biome) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, biome.id, key, biome);
		}
	}

	@Deprecated
	public static <T extends Biome> T register(int id, NamespacedIdentifier key, T biome) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (biome.id != id) {
				throw new IllegalArgumentException("ID " + id + " does not match biome ID " + biome.id + " for " + key);
			}

			return Registry.register(REGISTRY, id, key, biome);
		}
	}

	public static void init() {
		SyncedRegistries.register(RegistryKeys.BIOME);
		SyncedRegistries.registerFixer(RegistryKeys.BIOME, NamespacedIdentifiers.from("biome/id"), new BiomeIdFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerBiomes() {
		VanillaBiomes.init();
		BiomeEvents.REGISTER_BIOMES.invoker().run();
	}

	public static void registerUnknownBiomes() {
		for (Biome biome : Biome.BY_ID) {
			if (biome != null && REGISTRY.getIdentifier(biome) == null) {
				NamespacedIdentifier identifier = NamespacedIdentifiers.from("osl", "biome_" + biome.id);

				RegistriesImpl.LOGGER.warn("Biome {}/{}", biome.id, biome.getClass().getSimpleName() + " was not registered to OSL's Biome registry! Adding it as '" + identifier + "'...");
				BiomeRegistryImpl.register(identifier, biome);
			}
		}
	}
}
