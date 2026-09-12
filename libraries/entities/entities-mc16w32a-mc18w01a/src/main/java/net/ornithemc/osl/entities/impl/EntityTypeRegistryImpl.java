package net.ornithemc.osl.entities.impl;

import java.util.Set;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entities.SpawnEggData;
import net.minecraft.entity.Entity;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.api.EntityEvents;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

public final class EntityTypeRegistryImpl {

	public static final Registry<Class<? extends Entity>> REGISTRY = VanillaRegistries.registerSimple(RegistryKeys.ENTITY_TYPE, EntityTypeIdRegistry.REGISTRY, () -> Entities.REGISTRY);

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

	public static Class<? extends Entity> getEntityType(int id) {
		return REGISTRY.get(id);
	}

	public static Class<? extends Entity> getEntityType(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Class<? extends Entity> getEntityType(ResourceKey<Class<? extends Entity>> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Class<? extends Entity>>> keySet() {
		return REGISTRY.keySet();
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
			Identifier identifier = EntityTypeIdRegistry.REGISTRY.getKey(type);

			if (identifier == null) {
				throw new IllegalArgumentException("Entity type " + type.getSimpleName() + " is not registered!");
			}
			if (Entities.SPAWN_EGG_DATA.containsKey(identifier)) {
				throw new IllegalArgumentException("Duplicate entity type identifier " + identifier + " in spawn egg data registry!");
			}

			Entities.SPAWN_EGG_DATA.put(identifier, new SpawnEggData(identifier, baseColor, spotsColor));
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
