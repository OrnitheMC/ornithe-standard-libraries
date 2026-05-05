package net.ornithemc.osl.blocks.impl;

import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.BlockEvents;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public final class BlockRegistryImpl {

	private static boolean locked = true;
	private static boolean initialized = false;

	public static int getId(Block block) {
		return Block.REGISTRY.getId(block);
	}

	public static NamespacedIdentifier getKey(Block block) {
		String key = Block.REGISTRY.getKey(block);
		return key == null ? null : NamespacedIdentifiers.parse(key);
	}

	public static Block getBlock(int id) {
		return Block.REGISTRY.get(id);
	}

	public static Block getBlock(NamespacedIdentifier key) {
		return Block.REGISTRY.get(key.toString());
	}

	public static Set<NamespacedIdentifier> keySet() {
		return Block.REGISTRY.keySet().stream().map(NamespacedIdentifiers::parse).collect(Collectors.toSet());
	}

	public static <T extends Block> T register(int id, NamespacedIdentifier key, T block) {
		if (locked) {
			throw new IllegalStateException("register called too " + (initialized ? "late" : "early") + ": registry locked!");
		} else {
			Block.REGISTRY.register(id, key.toString(), block);
		}

		return block;
	}

	public static void lock() {
		if (!initialized) {
			throw new IllegalStateException("cannot lock block registry unless it's been initialized!");
		}

		locked = true;
	}

	public static void unlock() {
		if (initialized) {
			throw new IllegalStateException("cannot unlock block registry once it's been initialized!");
		}

		locked = false;
	}

	public static void init() {
		if (locked) {
			throw new IllegalStateException("cannot initialize block registry when it's locked!");
		}

		BlockEvents.REGISTER_BLOCKS.invoker().run();
		initialized = true;
	}
}
