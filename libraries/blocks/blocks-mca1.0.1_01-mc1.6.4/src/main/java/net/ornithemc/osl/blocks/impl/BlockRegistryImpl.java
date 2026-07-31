package net.ornithemc.osl.blocks.impl;

import java.util.Set;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.BlockEvents;
import net.ornithemc.osl.blocks.impl.block.BlockIdFixer;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.DefaultedRegistry;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public final class BlockRegistryImpl {

	public static final DefaultedRegistry<Block> REGISTRY = Registries.registerDefaulted(RegistryKeys.BLOCK, NamespacedIdentifiers.from("air"), () -> Block.BY_ID.getClass());

	private static boolean locked = true;

	public static int getId(Block block) {
		return REGISTRY.getId(block);
	}

	public static NamespacedIdentifier getIdentifier(Block block) {
		return REGISTRY.getIdentifier(block);
	}

	public static ResourceKey<Block> getKey(Block block) {
		return REGISTRY.getKey(block);
	}

	public static Block getBlock(int id) {
		return REGISTRY.get(id);
	}

	public static Block getBlock(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Block getBlock(ResourceKey<Block> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Block>> keySet() {
		return REGISTRY.keySet();
	}

	@SuppressWarnings("deprecation")
	public static <T extends Block> T register(NamespacedIdentifier identifier, T block) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, block.id, identifier, block);
		}
	}

	@SuppressWarnings("deprecation")
	public static <T extends Block> T register(ResourceKey<Block> key, T block) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, block.id, key, block);
		}
	}

	@Deprecated
	public static <T extends Block> T register(int id, NamespacedIdentifier key, T block) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (block.id != id) {
				throw new IllegalArgumentException("ID " + id + " does not match block ID " + block.id + " for " + key);
			}

			return Registry.register(REGISTRY, id, key, block);
		}
	}

	public static void init() {
		SyncedRegistries.register(RegistryKeys.BLOCK);
		SyncedRegistries.registerFixer(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block/id"), new BlockIdFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerBlocks() {
		VanillaBlocks.init();
		BlockEvents.REGISTER_BLOCKS.invoker().run();
	}

	@SuppressWarnings("deprecation")
	public static void registerUnknownBlocks() {
		for (Block block : Block.BY_ID) {
			if (block != null && !REGISTRY.has(block)) {
				NamespacedIdentifier identifier = NamespacedIdentifiers.from("osl", "block_" + block.id);

				RegistriesImpl.LOGGER.warn("Block {}/{}", block.id, block.getClass().getSimpleName() + " was not registered to OSL's Block registry! Adding it as '" + identifier + "'...");
				Registry.register(REGISTRY, block.id, identifier, block);
			}
		}
	}
}
