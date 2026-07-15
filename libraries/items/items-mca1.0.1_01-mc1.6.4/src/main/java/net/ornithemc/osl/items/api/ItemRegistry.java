package net.ornithemc.osl.items.api;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.impl.ItemRegistryImpl;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

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
	public static NamespacedIdentifier getIdentifier(Item item) {
		return ItemRegistryImpl.getIdentifier(item);
	}

	/**
	 * @return the resource key assigned to the given item.
	 */
	public static ResourceKey<Item> getKey(Item item) {
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
	public static Item getItem(NamespacedIdentifier identifier) {
		return ItemRegistryImpl.getItem(identifier);
	}

	/**
	 * @return the item mapped to the given resource key.
	 */
	public static Item getItem(ResourceKey<Item> key) {
		return ItemRegistryImpl.getItem(key);
	}

	/**
	 * @return a set containing all namespaced IDs in the registry.
	 */
	public static Set<NamespacedIdentifier> identifierSet() {
		return ItemRegistryImpl.identifierSet();
	}

	/**
	 * @return a set containing all resource keys in the registry.
	 */
	public static Set<ResourceKey<Item>> keySet() {
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
	 * @param <T>        the item type.
	 * @param identifier the namespaced ID of the item.
	 * @param item       the item to register.
	 * @return the registered item.
	 */
	public static <T extends Item> T register(NamespacedIdentifier identifier, T item) {
		return ItemRegistryImpl.register(identifier, item);
	}

	/**
	 * @param <T>   the item type.
	 * @param key   the resource key of the item.
	 * @param item  the item to register.
	 * @return the registered item.
	 */
	public static <T extends Item> T register(ResourceKey<Item> key, T item) {
		return ItemRegistryImpl.register(key, item);
	}

	/**
	 * @param <T>   the item type.
	 * @param id    the numerical ID of the item.
	 * @param key   the namespaced ID of the item.
	 * @param item  the item to register.
	 * @return the registered item.
	 * 
	 * @deprecated use {@linkplain #register(NamespacedIdentifier, Item)}
	 *             or {@linkplain #register(ResourceKey, Item)} instead.
	 */
	@Deprecated
	public static <T extends Item> T register(int id, NamespacedIdentifier key, T item) {
		return ItemRegistryImpl.register(id, key, item);
	}
}
