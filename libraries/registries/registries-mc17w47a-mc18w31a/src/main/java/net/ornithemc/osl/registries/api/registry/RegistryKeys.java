package net.ornithemc.osl.registries.api.registry;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

/**
 * Utility class containing commonly used registry keys (i.e. @linkplain
 * ResourceKey resource keys} for registries), as well as factory methods
 * for creating registry keys.
 */
public final class RegistryKeys {

	public static final ResourceKey<Registry<Block>> BLOCK = from("block");
	public static final ResourceKey<Registry<Item>> ITEM = from("item");
	public static final ResourceKey<Registry<EntityType<?>>> ENTITY_TYPE = from("entity_type");

	/**
	 * Constructs a registry key with the default namespace and the given identifier.
	 * 
	 * @param <T> the value type of the registry.
	 * @param identifier the identifier of the registry.
	 * @return the constructed registry key.
	 */
	public static <T> ResourceKey<Registry<T>> from(String identifier) {
		return from(NamespacedIdentifiers.from(identifier));
	}

	/**
	 * Constructs a registry key with the given namespaced identifier.
	 * 
	 * @param <T> the value type of the registry.
	 * @param identifier the namespaced identifier of the registry.
	 * @return the constructed registry key.
	 */
	public static <T> ResourceKey<Registry<T>> from(NamespacedIdentifier identifier) {
		return ResourceKeys.from(identifier);
	}
}
