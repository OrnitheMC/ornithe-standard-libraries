package net.ornithemc.osl.blocks.api;

import java.util.Set;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.DefaultedRegistry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

/**
 * Public access to the Blocks registry.
 */
public final class BlockRegistry {

	public static final DefaultedRegistry<Block> REGISTRY = BlockRegistryImpl.REGISTRY;

	/**
	 * @return the numerical ID assigned to the given block.
	 */
	public static int getId(Block block) {
		return BlockRegistryImpl.getId(block);
	}

	/**
	 * @return the namespaced ID assigned to the given block.
	 */
	public static NamespacedIdentifier getIdentifier(Block block) {
		return BlockRegistryImpl.getIdentifier(block);
	}

	/**
	 * @return the resource key assigned to the given block.
	 */
	public static ResourceKey<Block> getKey(Block block) {
		return BlockRegistryImpl.getKey(block);
	}

	/**
	 * @return the block mapped to the given numerical ID.
	 */
	public static Block getBlock(int id) {
		return BlockRegistryImpl.getBlock(id);
	}

	/**
	 * @return the block mapped to the given namespaced ID.
	 */
	public static Block getBlock(NamespacedIdentifier identifier) {
		return BlockRegistryImpl.getBlock(identifier);
	}

	/**
	 * @return the block mapped to the given resource key.
	 */
	public static Block getBlock(ResourceKey<Block> key) {
		return BlockRegistryImpl.getBlock(key);
	}

	/**
	 * @return a set containing all namespaced IDs in the registry.
	 */
	public static Set<NamespacedIdentifier> identifierSet() {
		return BlockRegistryImpl.identifierSet();
	}

	/**
	 * @return a set containing all resource keys in the registry.
	 */
	public static Set<ResourceKey<Block>> keySet() {
		return BlockRegistryImpl.keySet();
	}

	/**
	 * @param <T>        the block type.
	 * @param identifier the namespaced ID of the block.
	 * @param block      the block to register.
	 * @return the registered  block.
	 */
	public static <T extends Block> T register(NamespacedIdentifier identifier, T block) {
		return BlockRegistryImpl.register(identifier, block);
	}

	/**
	 * @param <T>   the block type.
	 * @param key   the resource key of the block.
	 * @param block the block to register.
	 * @return the registered  block.
	 */
	public static <T extends Block> T register(ResourceKey<Block> key, T block) {
		return BlockRegistryImpl.register(key, block);
	}
}
