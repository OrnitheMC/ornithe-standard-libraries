package net.ornithemc.osl.registries.api.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public final class RegistryKeys {

	// commonly used registry keys, for use in other APIs
	public static final ResourceKey<Registry<Block>> BLOCK = from("block");
	public static final ResourceKey<Registry<Item>> ITEM = from("item");

	public static <T> ResourceKey<Registry<T>> from(String identifier) {
		return from(NamespacedIdentifiers.from(identifier));
	}

	public static <T> ResourceKey<Registry<T>> from(NamespacedIdentifier identifier) {
		return ResourceKeys.from(identifier);
	}
}
