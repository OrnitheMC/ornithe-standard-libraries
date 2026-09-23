package net.ornithemc.osl.entities.api;

import java.util.Set;

import net.minecraft.entity.Entity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.api.entity.EntityType;
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
	 * @return the legacy {@code String} ID assigned to the given entity type.
	 */
	public static String getLegacyId(Class<? extends Entity> type) {
		return EntityTypeRegistryImpl.getLegacyId(type);
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
	 * @return the entity type mapped to the given legacy {@code String} ID.
	 */
	public static Class<? extends Entity> getEntityType(String legacyId) {
		return EntityTypeRegistryImpl.getEntityType(legacyId);
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
	 * @return a set containing all legacy {@code String} IDs in the registry.
	 */
	public static Set<String> legacyIdSet() {
		return EntityTypeRegistryImpl.legacyIdSet();
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

	/**
	 * @param <T>   the entity type.
	 * @param id    the numerical ID of the entity type.
	 * @param key   the namespaced ID of the entity type.
	 * @param type  the builder for the entity type to register.
	 * @return the registered entity type.
	 * 
	 * @deprecated use {@linkplain #register(NamespacedIdentifier, EntityType.Builder)}
	 *             or {@linkplain #register(ResourceKey, EntityType.Builder)} instead.
	 */
	@Deprecated
	public static <T extends Entity> EntityType<T> register(int id, NamespacedIdentifier key, EntityType.Builder<T> type) {
		return EntityTypeRegistryImpl.register(id, key, type);
	}
}
