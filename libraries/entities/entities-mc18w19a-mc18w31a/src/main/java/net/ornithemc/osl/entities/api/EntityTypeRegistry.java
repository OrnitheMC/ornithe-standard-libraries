package net.ornithemc.osl.entities.api;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

/**
 * Public access to the Entity Types registry.
 */
public final class EntityTypeRegistry {

	public static final Registry<EntityType<?>> REGISTRY = EntityTypeRegistryImpl.REGISTRY;

	/**
	 * @return the numerical ID assigned to the given entity type.
	 */
	public static int getId(EntityType<?> type) {
		return EntityTypeRegistryImpl.getId(type);
	}

	/**
	 * @return the namespaced ID assigned to the given entity type.
	 */
	public static NamespacedIdentifier getIdentifier(EntityType<?> type) {
		return EntityTypeRegistryImpl.getIdentifier(type);
	}

	/**
	 * @return the resource key assigned to the given entity type.
	 */
	public static ResourceKey<EntityType<?>> getKey(EntityType<?> type) {
		return EntityTypeRegistryImpl.getKey(type);
	}

	/**
	 * @return the entity type mapped to the given numerical ID.
	 */
	public static EntityType<?> getEntityType(int id) {
		return EntityTypeRegistryImpl.getEntityType(id);
	}

	/**
	 * @return the entity type mapped to the given namespaced ID.
	 */
	public static EntityType<?> getEntityType(NamespacedIdentifier identifier) {
		return EntityTypeRegistryImpl.getEntityType(identifier);
	}

	/**
	 * @return the entity type mapped to the given resource key.
	 */
	public static EntityType<?> getEntityType(ResourceKey<EntityType<?>> key) {
		return EntityTypeRegistryImpl.getEntityType(key);
	}

	/**
	 * @return a set containing all namespaced IDs in the registry.
	 */
	public static Set<NamespacedIdentifier> identifierSet() {
		return EntityTypeRegistryImpl.identifierSet();
	}

	/**
	 * @return a set containing all resource keys in the registry.
	 */
	public static Set<ResourceKey<EntityType<?>>> keySet() {
		return EntityTypeRegistryImpl.keySet();
	}

	/**
	 * @param <T>        the entity type.
	 * @param identifier the namespaced ID of the entity type.
	 * @param type       the builder for the entity type to register.
	 * @return the registered entity type.
	 */
	public static <T extends Entity> EntityType<T> register(NamespacedIdentifier identifier, EntityType.Builder<T> type) {
		return EntityTypeRegistryImpl.register(identifier, type);
	}

	/**
	 * @param <T>   the entity type.
	 * @param key   the resource key of the entity type.
	 * @param type  the builder for the entity type to register.
	 * @return the registered entity type.
	 */
	public static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> type) {
		return EntityTypeRegistryImpl.register(key, type);
	}
}
