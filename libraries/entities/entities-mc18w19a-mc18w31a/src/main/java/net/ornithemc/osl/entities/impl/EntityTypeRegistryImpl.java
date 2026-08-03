package net.ornithemc.osl.entities.impl;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.api.EntityEvents;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

public final class EntityTypeRegistryImpl {

	public static final Registry<EntityType<?>> REGISTRY = VanillaRegistries.registerSimple(RegistryKeys.ENTITY_TYPE, EntityTypeIdRegistry.REGISTRY, () -> EntityType.REGISTRY);

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

	public static EntityType<?> getEntityType(int id) {
		return REGISTRY.get(id);
	}

	public static EntityType<?> getEntityType(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static EntityType<?> getEntityType(ResourceKey<EntityType<?>> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<EntityType<?>>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends Entity> EntityType<T> register(NamespacedIdentifier identifier, EntityType.Builder<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, type.build(null));
		}
	}

	public static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, key, type.build(null));
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
