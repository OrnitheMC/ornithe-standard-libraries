package net.ornithemc.osl.items.api.item;

import net.minecraft.item.Item;

import net.ornithemc.osl.items.impl.ItemRegistryImpl;
import net.ornithemc.osl.registries.api.registry.Registry;

public interface ItemExtension {

	Registry<Item> REGISTRY = ItemRegistryImpl.REGISTRY;

}
