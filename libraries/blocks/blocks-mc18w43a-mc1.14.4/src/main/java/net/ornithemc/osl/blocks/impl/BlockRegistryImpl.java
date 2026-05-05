package net.ornithemc.osl.blocks.impl;

import java.util.Collections;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.Registry;

import net.ornithemc.osl.blocks.api.BlockEvents;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public final class BlockRegistryImpl {

	private static boolean locked = true;
	private static boolean initialized = false;

	public static int getId(Block block) {
		return Registry.BLOCK.getId(block);
	}

	public static NamespacedIdentifier getKey(Block block) {
		return Registry.BLOCK.getKey(block);
	}

	public static Block getBlock(int id) {
		return Registry.BLOCK.get(id);
	}

	public static Block getBlock(NamespacedIdentifier key) {
		return Registry.BLOCK.getOrDefault(identifier(key));
	}

	public static Set<NamespacedIdentifier> keySet() {
		return Collections.unmodifiableSet(Registry.BLOCK.keySet());
	}

	public static <T extends Block> T register(NamespacedIdentifier key, T block) {
		if (locked) {
			throw new IllegalStateException("register called too " + (initialized ? "late" : "early") + ": registry locked!");
		} else {
			return Registry.BLOCK.m_30368144(identifier(key), block);
		}
	}

	private static Identifier identifier(NamespacedIdentifier id) {
		return id instanceof Identifier ? (Identifier) id : new Identifier(id.namespace(), id.identifier());
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
