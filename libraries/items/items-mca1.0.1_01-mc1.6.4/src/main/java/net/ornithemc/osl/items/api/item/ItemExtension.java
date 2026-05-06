package net.ornithemc.osl.items.api.item;

import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.registry.SimpleIdRegistry;

public interface ItemExtension {

	SimpleIdRegistry<Item> REGISTRY = new SimpleIdRegistry<>();

}
