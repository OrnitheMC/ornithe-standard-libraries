package net.ornithemc.osl.items.impl;

import net.minecraft.item.ItemStack;

public interface LootEntryAccess {

	ItemStack osl$items$getItemStack();

	int osl$items$getItem();

	void osl$items$setItem(int item);

}
