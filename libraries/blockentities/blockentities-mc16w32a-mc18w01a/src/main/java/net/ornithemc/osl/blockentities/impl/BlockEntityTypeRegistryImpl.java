package net.ornithemc.osl.blockentities.impl;

import java.util.Set;

import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.blockentities.api.BlockEntityEvents;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

public final class BlockEntityTypeRegistryImpl {

	public static final Registry<Class<? extends BlockEntity>> REGISTRY = VanillaRegistries.registerSimple(RegistryKeys.BLOCK_ENTITY_TYPE, BlockEntityTypeIdRegistry.REGISTRY, () -> BlockEntity.REGISTRY);

	private static boolean locked = true;

	public static int getId(Class<? extends BlockEntity> type) {
		return REGISTRY.getId(type);
	}

	public static NamespacedIdentifier getIdentifier(Class<? extends BlockEntity> type) {
		return REGISTRY.getIdentifier(type);
	}

	public static ResourceKey<Class<? extends BlockEntity>> getKey(Class<? extends BlockEntity> type) {
		return REGISTRY.getKey(type);
	}

	public static Class<? extends BlockEntity> getBlockEntityType(int id) {
		return REGISTRY.get(id);
	}

	public static Class<? extends BlockEntity> getBlockEntityType(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Class<? extends BlockEntity> getBlockEntityType(ResourceKey<Class<? extends BlockEntity>> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Class<? extends BlockEntity>>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends BlockEntity> Class<T> register(NamespacedIdentifier identifier, Class<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, type);
		}
	}

	public static <T extends BlockEntity> Class<T> register(ResourceKey<Class<? extends BlockEntity>> key, Class<T> type) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, key, type);
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
