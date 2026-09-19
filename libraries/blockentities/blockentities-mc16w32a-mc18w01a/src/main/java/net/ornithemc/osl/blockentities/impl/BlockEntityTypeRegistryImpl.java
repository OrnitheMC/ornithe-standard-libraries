package net.ornithemc.osl.blockentities.impl;

import java.util.Set;

import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.blockentities.api.BlockEntityEvents;
import net.ornithemc.osl.blockentities.api.blockentity.BlockEntityType;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public final class BlockEntityTypeRegistryImpl {

	public static final WrappedBlockEntityTypeRegistry REGISTRY = RegistriesImpl.register(RegistryKeys.BLOCK_ENTITY_TYPE, new WrappedBlockEntityTypeRegistry(RegistryKeys.BLOCK_ENTITY_TYPE.identifier()), () -> BlockEntity.REGISTRY.getClass());

	private static boolean locked = true;

	public static int getId(BlockEntityType<?> type) {
		return REGISTRY.getId(type);
	}

	public static NamespacedIdentifier getIdentifier(BlockEntityType<?> type) {
		return REGISTRY.getIdentifier(type);
	}

	public static ResourceKey<BlockEntityType<?>> getKey(BlockEntityType<?> type) {
		return REGISTRY.getKey(type);
	}

	public static BlockEntityType<?> getBlockEntityType(int id) {
		return REGISTRY.get(id);
	}

	public static BlockEntityType<?> getBlockEntityType(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static BlockEntityType<?> getBlockEntityType(ResourceKey<BlockEntityType<?>> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<BlockEntityType<?>>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(NamespacedIdentifier identifier, BlockEntityType.Builder<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, type.build());
		}
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(ResourceKey<BlockEntityType<?>> key, BlockEntityType.Builder<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, key, type.build());
		}
	}

	public static void init() {
		SyncedRegistries.register(RegistryKeys.BLOCK_ENTITY_TYPE);
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerBlockEntityTypes() {
		BlockEntityEvents.REGISTER_BLOCK_ENTITY_TYPES.invoker().run();
	}
}
