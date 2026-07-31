package net.ornithemc.osl.blockentities.api;

import java.util.Set;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import net.ornithemc.osl.blockentities.impl.BlockEntityTypeRegistryImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

/**
 * Public access to the Block Entity Types registry.
 */
public final class BlockEntityTypeRegistry {

	public static final Registry<BlockEntityType<?>> REGISTRY = BlockEntityTypeRegistryImpl.REGISTRY;

	/**
	 * @return the numerical ID assigned to the given block entity type.
	 */
	public static int getId(BlockEntityType<?> type) {
		return BlockEntityTypeRegistryImpl.getId(type);
	}

	/**
	 * @return the namespaced ID assigned to the given block entity type.
	 */
	public static NamespacedIdentifier getIdentifier(BlockEntityType<?> type) {
		return BlockEntityTypeRegistryImpl.getIdentifier(type);
	}

	/**
	 * @return the resource key assigned to the given block entity type.
	 */
	public static ResourceKey<BlockEntityType<?>> getKey(BlockEntityType<?> type) {
		return BlockEntityTypeRegistryImpl.getKey(type);
	}

	/**
	 * @return the block entity type mapped to the given numerical ID.
	 */
	public static BlockEntityType<?> getBlockEntityType(int id) {
		return BlockEntityTypeRegistryImpl.getBlockEntityType(id);
	}

	/**
	 * @return the block entity type mapped to the given namespaced ID.
	 */
	public static BlockEntityType<?> getBlockEntityType(NamespacedIdentifier identifier) {
		return BlockEntityTypeRegistryImpl.getBlockEntityType(identifier);
	}

	/**
	 * @return the block entity type mapped to the given resource key.
	 */
	public static BlockEntityType<?> getBlockEntityType(ResourceKey<BlockEntityType<?>> key) {
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
	public static Set<ResourceKey<BlockEntityType<?>>> keySet() {
		return BlockEntityTypeRegistryImpl.keySet();
	}

	/**
	 * @param <T>        the block entity type.
	 * @param identifier the namespaced ID of the block entity type.
	 * @param type       the builder for the block entity type to register.
	 * @return the registered block entity type.
	 */
	public static <T extends BlockEntity> BlockEntityType<T> register(NamespacedIdentifier identifier, BlockEntityType.Builder<T> type) {
		return BlockEntityTypeRegistryImpl.register(identifier, type);
	}

	/**
	 * @param <T>   the block entity type.
	 * @param key   the resource key of the block entity type.
	 * @param type  the builder for the block entity type to register.
	 * @return the registered block entity type.
	 */
	public static <T extends BlockEntity> BlockEntityType<T> register(ResourceKey<BlockEntityType<?>> key, BlockEntityType.Builder<T> type) {
		return BlockEntityTypeRegistryImpl.register(key, type);
	}
}
