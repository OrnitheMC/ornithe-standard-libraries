package net.ornithemc.osl.items.impl;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.api.ItemEvents;
import net.ornithemc.osl.items.impl.mixin.common.BlockItemAccess;

public final class ItemRegistryImpl {

	private static boolean locked = true;
	private static boolean itemsInitialized = false;
	private static boolean blocksInitialized = false;
	private static boolean initialized = false;

	public static int getId(Item item) {
		return Item.REGISTRY.getId(item);
	}

	public static NamespacedIdentifier getKey(Item item) {
		return Item.REGISTRY.getKey(item);
	}

	public static Item getItem(int id) {
		return Item.REGISTRY.get(id);
	}

	public static Item getItem(NamespacedIdentifier key) {
		return Item.REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> keySet() {
		return Item.REGISTRY.keySet();
	}

	public static BlockItem register(Block block) {
		return register(block, new BlockItem(BlockRegistry.getId(block)));
	}

	public static <T extends BlockItem> T register(T item) {
		return register(BlockRegistry.getBlock(((BlockItemAccess) item).accessBlock()), item);
	}

	public static <T extends Item> T register(Block block, T item) {
		return register(BlockRegistry.getId(block), BlockRegistry.getKey(block), item);
	}

	public static <T extends Item> T register(int id, NamespacedIdentifier key, T item) {
		if (locked) {
			throw new IllegalStateException("register called too " + (initialized ? "late" : "early") + ": registry locked!");
		} else {
			Item.REGISTRY.register(id, key, item);
		}

		return item;
	}

	public static void lock() {
		if (!itemsInitialized || !blocksInitialized) {
			throw new IllegalStateException("cannot lock item registry unless it's been initialized!");
		}

		locked = true;
	}

	public static void unlock() {
		if (itemsInitialized || blocksInitialized) {
			throw new IllegalStateException("cannot unlock item registry once it's been initialized!");
		}

		locked = false;
	}

	public static void initItems() {
		if (locked) {
			throw new IllegalStateException("cannot initialize items when the registry is locked!");
		}

		VanillaItems.init();
		itemsInitialized = true;
	}

	public static void initBlocks() {
		if (locked) {
			throw new IllegalStateException("cannot initialize block items when the registry is locked!");
		}

		VanillaBlockItems.init();
		blocksInitialized = true;
	}

	public static void init() {
		if (locked) {
			throw new IllegalStateException("cannot initialize item registry when it's locked!");
		}

		ItemEvents.REGISTER_ITEMS.invoker().run();
		initialized = true;
	}

	public static boolean shouldInitialize() {
		return itemsInitialized && blocksInitialized && !initialized;
	}
}
