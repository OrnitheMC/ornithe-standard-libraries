package net.ornithemc.osl.entities.api;

import java.util.Set;

import net.minecraft.entity.Entity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

/**
 * Public access to the Entity Types registry.
 */
public final class EntityTypeRegistry {

	public static final Registry<Class<? extends Entity>> REGISTRY = EntityTypeRegistryImpl.REGISTRY;

	/**
	 * @return the numerical ID assigned to the given entity type.
	 */
	public static int getId(Class<? extends Entity> type) {
		return EntityTypeRegistryImpl.getId(type);
	}

	/**
	 * @return the namespaced ID assigned to the given entity type.
	 */
	public static NamespacedIdentifier getIdentifier(Class<? extends Entity> type) {
		return EntityTypeRegistryImpl.getIdentifier(type);
	}

	/**
	 * @return the resource key assigned to the given entity type.
	 */
	public static ResourceKey<Class<? extends Entity>> getKey(Class<? extends Entity> type) {
		return EntityTypeRegistryImpl.getKey(type);
	}

	/**
	 * @return the legacy {@code String} key assigned to the given entity type.
	 */
	public static String getLegacyKey(Class<? extends Entity> type) {
		return EntityTypeRegistryImpl.getLegacyKey(type);
	}

	/**
	 * @return the entity type mapped to the given numerical ID.
	 */
	public static Class<? extends Entity> getEntityType(int id) {
		return EntityTypeRegistryImpl.getEntityType(id);
	}

	/**
	 * @return the entity type mapped to the given namespaced ID.
	 */
	public static Class<? extends Entity> getEntityType(NamespacedIdentifier identifier) {
		return EntityTypeRegistryImpl.getEntityType(identifier);
	}

	/**
	 * @return the entity type mapped to the given resource key.
	 */
	public static Class<? extends Entity> getEntityType(ResourceKey<Class<? extends Entity>> key) {
		return EntityTypeRegistryImpl.getEntityType(key);
	}

	/**
	 * @return the entity type mapped to the given legacy {@code String} key.
	 */
	public static Class<? extends Entity> getEntityType(String legacyKey) {
		return EntityTypeRegistryImpl.getEntityType(legacyKey);
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
	public static Set<ResourceKey<Class<? extends Entity>>> keySet() {
		return EntityTypeRegistryImpl.keySet();
	}

	/**
	 * @return a set containing all legacy {@code String} keys in the registry.
	 */
	public static Set<String> legacyKeySet() {
		return EntityTypeRegistryImpl.legacyKeySet();
	}

	/**
	 * @param <T>        the entity type.
	 * @param identifier the namespaced ID of the entity type.
	 * @param type       the entity type to register.
	 * @return the registered entity type.
	 */
	public static <T extends Entity> Class<T> register(NamespacedIdentifier identifier, Class<T> type) {
		return EntityTypeRegistryImpl.register(identifier, type);
	}

	/**
	 * @param <T>   the entity type.
	 * @param key   the resource key of the entity type.
	 * @param type  the entity type to register.
	 * @return the registered entity type.
	 */
	public static <T extends Entity> Class<T> register(ResourceKey<Class<? extends Entity>> key, Class<T> type) {
		return EntityTypeRegistryImpl.register(key, type);
	}

	/**
	 * Registers spawn egg data for the specified entity type.
	 * 
	 * @param identifier the identifier of the entity type for which to register the spawn egg data.
	 * @param baseColor  the base color of the spawn egg.
	 * @param spotsColor the color of the spots on the spawn egg.
	 */
	public static void registerSpawnEggData(NamespacedIdentifier identifier, int baseColor, int spotsColor) {
		EntityTypeRegistryImpl.registerSpawnEggData(identifier, baseColor, spotsColor);
	}

	/**
	 * Registers spawn egg data for the specified entity type.
	 * 
	 * @param key        the resource key of the entity type for which to register the spawn egg data.
	 * @param baseColor  the base color of the spawn egg.
	 * @param spotsColor the color of the spots on the spawn egg.
	 */
	public static void registerSpawnEggData(ResourceKey<Class<? extends Entity>> key, int baseColor, int spotsColor) {
		EntityTypeRegistryImpl.registerSpawnEggData(key, baseColor, spotsColor);
	}

	/**
	 * Registers spawn egg data for the specified entity type.
	 * 
	 * @param type       the entity type for which to register the spawn egg data.
	 * @param baseColor  the base color of the spawn egg.
	 * @param spotsColor the color of the spots on the spawn egg.
	 */
	public static void registerSpawnEggData(Class<? extends Entity> type, int baseColor, int spotsColor) {
		EntityTypeRegistryImpl.registerSpawnEggData(type, baseColor, spotsColor);
	}
}
