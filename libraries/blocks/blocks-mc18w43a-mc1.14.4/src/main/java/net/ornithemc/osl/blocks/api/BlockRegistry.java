package net.ornithemc.osl.blocks.api;

import java.util.Set;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * Public access to the Blocks registry.
 */
public final class BlockRegistry {

	/**
	 * @return the numerical ID assigned to the given block.
	 */
	public static int getId(Block block) {
		return BlockRegistryImpl.getId(block);
	}

	/**
	 * @return the namespaced ID assigned to the given block.
	 */
	public static NamespacedIdentifier getKey(Block block) {
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
	public static Block getBlock(NamespacedIdentifier key) {
		return BlockRegistryImpl.getBlock(key);
	}

	/**
	 * @return a set containing all namespaced IDs in the registry.
	 */
	public static Set<NamespacedIdentifier> keySet() {
		return BlockRegistryImpl.keySet();
	}

	/**
	 * @param <T>   the block type.
	 * @param key   the namespaced ID of the block.
	 * @param block the block to register.
	 * @return the registered  block.
	 */
	public static <T extends Block> T register(NamespacedIdentifier key, T block) {
		return BlockRegistryImpl.register(key, block);
	}
}
