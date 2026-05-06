package net.ornithemc.osl.items.impl;

import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.items.api.ItemEvents;
import net.ornithemc.osl.items.impl.mixin.common.BlockItemAccess;

public final class ItemRegistryImpl {

	private static boolean locked = true;
	private static boolean initialized = false;

	public static int getId(Item item) {
		return Item.REGISTRY.getId(item);
	}

	public static NamespacedIdentifier getKey(Item item) {
		String key = Item.REGISTRY.getKey(item);
		return key == null ? null : NamespacedIdentifiers.parse(key);
	}

	public static Item getItem(int id) {
		return Item.REGISTRY.get(id);
	}

	public static Item getItem(NamespacedIdentifier key) {
		return Item.REGISTRY.get(key.toString());
	}

	public static Set<NamespacedIdentifier> keySet() {
		return Item.REGISTRY.keySet().stream().map(NamespacedIdentifiers::parse).collect(Collectors.toSet());
	}

	public static BlockItem register(Block block) {
		return register(block, new BlockItem(block));
	}

	public static <T extends BlockItem> T register(T item) {
		return register(((BlockItemAccess) item).accessBlock(), item);
	}

	public static <T extends Item> T register(Block block, T item) {
		return register(BlockRegistry.getId(block), BlockRegistry.getKey(block), item);
	}

	public static <T extends Item> T register(int id, NamespacedIdentifier key, T item) {
		if (locked) {
			throw new IllegalStateException("register called too " + (initialized ? "late" : "early") + ": registry locked!");
		} else {
			Item.REGISTRY.register(id, key.toString(), item);
		}

		return item;
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
