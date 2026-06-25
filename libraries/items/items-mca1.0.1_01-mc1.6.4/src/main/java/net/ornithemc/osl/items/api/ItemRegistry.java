package net.ornithemc.osl.items.api;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.impl.ItemRegistryImpl;

/**
 * Public access to the Items registry.
 */
public final class ItemRegistry {

	/**
	 * @return the numerical ID assigned to the given item.
	 */
	public static int getId(Item item) {
		return ItemRegistryImpl.getId(item);
	}

	/**
	 * @return the namespaced ID assigned to the given item.
	 */
	public static NamespacedIdentifier getKey(Item item) {
		return ItemRegistryImpl.getKey(item);
	}

	/**
	 * @return the item mapped to the given numerical ID.
	 */
	public static Item getItem(int id) {
		return ItemRegistryImpl.getItem(id);
	}

	/**
	 * @return the item mapped to the given namespaced ID.
	 */
	public static Item getItem(NamespacedIdentifier key) {
		return ItemRegistryImpl.getItem(key);
	}

	/**
	 * @return a set containing all namespaced IDs in the registry.
	 */
	public static Set<NamespacedIdentifier> keySet() {
		return ItemRegistryImpl.keySet();
	}

	/**
	 * @param block the block item to register.
	 * @return the registered block item.
	 */
	public static BlockItem register(Block block) {
		return ItemRegistryImpl.register(block);
	}

	/**
	 * @param <T>   the item type.
	 * @param item  the item to register.
	 * @return the registered block item.
	 */
	public static <T extends BlockItem> T register(T item) {
		return ItemRegistryImpl.register(item);
	}

	/**
	 * @param <T>   the item type.
	 * @param block the block placed by the item.
	 * @param item  the item to register.
	 * @return the registered item.
	 */
	public static <T extends Item> T register(Block block, T item) {
		return ItemRegistryImpl.register(block, item);
	}

	/**
	 * @param <T>   the item type.
	 * @param id    the numerical ID of the item.
	 * @param key   the namespaced ID of the item.
	 * @param item  the item to register.
	 * @return the registered item.
	 */
	public static <T extends Item> T register(int id, NamespacedIdentifier key, T item) {
		return ItemRegistryImpl.register(id, key, item);
	}
}
