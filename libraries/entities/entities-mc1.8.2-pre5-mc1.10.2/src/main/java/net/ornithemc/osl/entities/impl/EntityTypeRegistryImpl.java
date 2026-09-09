package net.ornithemc.osl.entities.impl;

import java.util.Set;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.api.EntityEvents;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public final class EntityTypeRegistryImpl {

	public static final WrappedEntityTypeRegistry REGISTRY = RegistriesImpl.register(RegistryKeys.ENTITY_TYPE, new WrappedEntityTypeRegistry(RegistryKeys.ENTITY_TYPE.identifier()), () -> Entities.KEY_TO_TYPE.getClass());
	/*
	 * Entities.SPAWN_EGG_DATA changes key type from Integer to String in 15w33a so we can't reference it directly.
	 */
	public static SpawnEggDataRegistry SPAWN_EGG_DATA_REGISTRY;

	private static boolean locked = true;

	public static int getId(Class<? extends Entity> type) {
		return REGISTRY.getId(type);
	}

	public static NamespacedIdentifier getIdentifier(Class<? extends Entity> type) {
		return REGISTRY.getIdentifier(type);
	}

	public static ResourceKey<Class<? extends Entity>> getKey(Class<? extends Entity> type) {
		return REGISTRY.getKey(type);
	}

	public static String getLegacyKey(Class<? extends Entity> type) {
		return REGISTRY.getLegacyKey(type);
	}

	public static Class<? extends Entity> getEntityType(int id) {
		return REGISTRY.get(id);
	}

	public static Class<? extends Entity> getEntityType(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Class<? extends Entity> getEntityType(ResourceKey<Class<? extends Entity>> key) {
		return REGISTRY.get(key);
	}

	public static Class<? extends Entity> getEntityType(String legacyKey) {
		return REGISTRY.get(legacyKey);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Class<? extends Entity>>> keySet() {
		return REGISTRY.keySet();
	}

	public static Set<String> legacyKeySet() {
		return REGISTRY.legacyKeySet();
	}

	public static <T extends Entity> Class<T> register(NamespacedIdentifier identifier, Class<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, type);
		}
	}

	public static <T extends Entity> Class<T> register(ResourceKey<Class<? extends Entity>> key, Class<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, key, type);
		}
	}

	public static void registerSpawnEggData(NamespacedIdentifier identifier, int baseColor, int spotsColor) {
		registerSpawnEggData(getEntityType(identifier), baseColor, spotsColor);
	}

	public static void registerSpawnEggData(ResourceKey<Class<? extends Entity>> key, int baseColor, int spotsColor) {
		registerSpawnEggData(getEntityType(key), baseColor, spotsColor);
	}

	public static void registerSpawnEggData(Class<? extends Entity> type, int baseColor, int spotsColor) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			SPAWN_EGG_DATA_REGISTRY.register(type, baseColor, spotsColor);
		}
	}

	public static void init() {
		SyncedRegistries.register(RegistryKeys.ENTITY_TYPE);
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerEntityTypes() {
		EntityEvents.REGISTER_ENTITY_TYPES.invoker().run();
	}
}
