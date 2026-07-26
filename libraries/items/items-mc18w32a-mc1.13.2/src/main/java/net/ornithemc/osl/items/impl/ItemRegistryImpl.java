package net.ornithemc.osl.items.impl;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.api.ItemEvents;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

public final class ItemRegistryImpl {

	public static final Registry<Item> REGISTRY = VanillaRegistries.registerSimple(RegistryKeys.ITEM, net.minecraft.util.registry.Registry.ITEM);

	private static boolean locked = true;

	public static int getId(Item item) {
		return REGISTRY.getId(item);
	}

	public static NamespacedIdentifier getIdentifier(Item item) {
		return REGISTRY.getIdentifier(item);
	}

	public static ResourceKey<Item> getKey(Item item) {
		return REGISTRY.getKey(item);
	}

	public static Item getItem(int id) {
		return REGISTRY.get(id);
	}

	public static Item getItem(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Item getItem(ResourceKey<Item> key) {
		return REGISTRY.get(key);
	}

	public static Item getItem(Block block) {
		return Item.byBlock(block);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Item>> keySet() {
		return REGISTRY.keySet();
	}

	public static BlockItem register(Block block) {
		return register(block, new BlockItem(block, new Item.Properties()));
	}

	public static <T extends BlockItem> T register(T item) {
		return register(item.getBlock(), item);
	}

	public static <T extends Item> T register(Block block, T item) {
		return register(BlockRegistry.getIdentifier(block), item);
	}

	public static <T extends Item> T register(NamespacedIdentifier identifier, T item) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (item instanceof BlockItem) {
				((BlockItem) item).register(Item.BLOCK_ITEMS, item);
			}

			return Registry.register(REGISTRY, identifier, item);
		}
	}

	public static <T extends Item> T register(ResourceKey<Item> key, T item) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (item instanceof BlockItem) {
				((BlockItem) item).register(Item.BLOCK_ITEMS, item);
			}

			return Registry.register(REGISTRY, key, item);
		}
	}

	@Deprecated
	public static <T extends Item> T register(int id, NamespacedIdentifier key, T item) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (item instanceof BlockItem) {
				((BlockItem) item).register(Item.BLOCK_ITEMS, item);
			}

			return Registry.register(REGISTRY, id, key, item);
		}
	}

	public static void init() {
		SyncedRegistries.register(RegistryKeys.ITEM);
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerItems() {
		ItemEvents.REGISTER_ITEMS.invoker().run();
	}
}
