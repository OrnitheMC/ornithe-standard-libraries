package net.ornithemc.osl.blockentities.api;

import java.util.Set;

import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.blockentities.impl.BlockEntityTypeRegistryImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

/**
 * Public access to the Block Entity Types registry.
 */
public final class BlockEntityTypeRegistry {

	public static final Registry<Class<? extends BlockEntity>> REGISTRY = BlockEntityTypeRegistryImpl.REGISTRY;

	/**
	 * @return the numerical ID assigned to the given block entity type.
	 */
	public static int getId(Class<? extends BlockEntity> type) {
		return BlockEntityTypeRegistryImpl.getId(type);
	}

	/**
	 * @return the namespaced ID assigned to the given block entity type.
	 */
	public static NamespacedIdentifier getIdentifier(Class<? extends BlockEntity> type) {
		return BlockEntityTypeRegistryImpl.getIdentifier(type);
	}

	/**
	 * @return the resource key assigned to the given block entity type.
	 */
	public static ResourceKey<Class<? extends BlockEntity>> getKey(Class<? extends BlockEntity> type) {
		return BlockEntityTypeRegistryImpl.getKey(type);
	}

	/**
	 * @return the block entity type mapped to the given numerical ID.
	 */
	public static Class<? extends BlockEntity> getBlockEntityType(int id) {
		return BlockEntityTypeRegistryImpl.getBlockEntityType(id);
	}

	/**
	 * @return the block entity type mapped to the given namespaced ID.
	 */
	public static Class<? extends BlockEntity> getBlockEntityType(NamespacedIdentifier identifier) {
		return BlockEntityTypeRegistryImpl.getBlockEntityType(identifier);
	}

	/**
	 * @return the block entity type mapped to the given resource key.
	 */
	public static Class<? extends BlockEntity> getBlockEntityType(ResourceKey<Class<? extends BlockEntity>> key) {
		return BlockEntityTypeRegistryImpl.getBlockEntityType(key);
	}

	/**
	 * @return a set containing all namespaced IDs in the registry.
	 */
	public static Set<NamespacedIdentifier> identifierSet() {
		return BlockEntityTypeRegistryImpl.identifierSet();
	}

	/**
	 * @return a set containing all resource keys in the registry.
	 */
	public static Set<ResourceKey<Class<? extends BlockEntity>>> keySet() {
		return BlockEntityTypeRegistryImpl.keySet();
	}

	/**
	 * @param <T>        the block entity type.
	 * @param identifier the namespaced ID of the block entity type.
	 * @param type       the block entity type to register.
	 * @return the registered block entity type.
	 */
	public static <T extends BlockEntity> Class<T> register(NamespacedIdentifier identifier, Class<T> type) {
		return BlockEntityTypeRegistryImpl.register(identifier, type);
	}

	/**
	 * @param <T>   the block entity type.
	 * @param key   the resource key of the block entity type.
	 * @param type  the block entity type to register.
	 * @return the registered block entity type.
	 */
	public static <T extends BlockEntity> Class<T> register(ResourceKey<Class<? extends BlockEntity>> key, Class<T> type) {
		return BlockEntityTypeRegistryImpl.register(key, type);
	}
}
