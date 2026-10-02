package net.ornithemc.osl.entities.impl;

import java.util.Set;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entities.SpawnEggData;
import net.minecraft.entity.Entity;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.entities.api.EntityEvents;
import net.ornithemc.osl.entities.api.entity.EntityType;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public final class EntityTypeRegistryImpl {

	public static final WrappedEntityTypeRegistry REGISTRY = RegistriesImpl.register(RegistryKeys.ENTITY_TYPE, new WrappedEntityTypeRegistry(RegistryKeys.ENTITY_TYPE.identifier()), () -> Entities.REGISTRY.getClass());

	private static final boolean SPAWN_EGG_DATA_REGISTRY_EXISTS = MinecraftVersion.resolve().compareTo("17w47a") < 0;

	private static boolean locked = true;

	public static int getId(EntityType<?> type) {
		return REGISTRY.getId(type);
	}

	public static NamespacedIdentifier getIdentifier(EntityType<?> type) {
		return REGISTRY.getIdentifier(type);
	}

	public static ResourceKey<EntityType<?>> getKey(EntityType<?> type) {
		return REGISTRY.getKey(type);
	}

	public static String getLegacyId(Class<? extends Entity> type) {
		return REGISTRY.getLegacyId(type);
	}

	public static EntityType<?> getEntityType(int id) {
		return REGISTRY.get(id);
	}

	public static EntityType<?> getEntityType(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static EntityType<?> getEntityType(ResourceKey<EntityType<?>> key) {
		return REGISTRY.get(key);
	}

	public static Class<? extends Entity> getEntityType(String legacyId) {
		return REGISTRY.get(legacyId);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<EntityType<?>>> keySet() {
		return REGISTRY.keySet();
	}

	public static Set<String> legacyIdSet() {
		return REGISTRY.legacyIdSet();
	}

	public static <T extends Entity> EntityType<T> register(NamespacedIdentifier identifier, EntityType.Builder<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, type.build());
		}
	}

	public static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, key, type.build());
		}
	}

	public static void registerSpawnEgg(EntityType<?> type, int baseColor, int spotsColor) {
		if (!SPAWN_EGG_DATA_REGISTRY_EXISTS) {
			throw new UnsupportedOperationException("There is no spawn egg registry in this version! You must register your spawn eggs through the item registry.");
		} else if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			ResourceKey<EntityType<?>> key = REGISTRY.getKey(type);

			if (key == null) {
				throw new IllegalArgumentException("Entity type " + type.getType().getSimpleName() + " is not registered!");
			}

			Identifier identifier = new Identifier(key.identifier().namespace(), key.identifier().identifier());

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
