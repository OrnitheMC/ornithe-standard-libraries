package net.ornithemc.osl.items.impl;

import java.util.Collections;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.Registry;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.api.ItemEvents;

public final class ItemRegistryImpl {

	private static boolean locked = true;
	private static boolean initialized = false;

	public static int getId(Item item) {
		return Registry.ITEM.getId(item);
	}

	public static NamespacedIdentifier getKey(Item item) {
		return Registry.ITEM.getKey(item);
	}

	public static Item getItem(int id) {
		return Registry.ITEM.get(id);
	}

	public static Item getItem(NamespacedIdentifier key) {
		return Registry.ITEM.get(identifier(key));
	}

	public static Set<NamespacedIdentifier> keySet() {
		return Collections.unmodifiableSet(Registry.ITEM.keySet());
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

	public static <T extends Item> T register(NamespacedIdentifier key, T item) {
		if (locked) {
			throw new IllegalStateException("register called too " + (initialized ? "late" : "early") + ": registry locked!");
		} else {
			if (item instanceof BlockItem) {
				((BlockItem) item).register(Item.BLOCK_ITEMS, item);
			}

			Registry.ITEM.register(identifier(key), item);
		}

		return item;
	}

	private static Identifier identifier(NamespacedIdentifier id) {
		return id instanceof Identifier ? (Identifier) id : new Identifier(id.namespace(), id.identifier());
	}

	public static void lock() {
		if (!initialized) {
			throw new IllegalStateException("cannot lock item registry unless it's been initialized!");
		}

		locked = true;
	}

	public static void unlock() {
		if (initialized) {
			throw new IllegalStateException("cannot unlock item registry once it's been initialized!");
		}

		locked = false;
	}

	public static void init() {
		if (locked) {
			throw new IllegalStateException("cannot initialize item registry when it's locked!");
		}

		ItemEvents.REGISTER_ITEMS.invoker().run();
		initialized = true;
	}
}
