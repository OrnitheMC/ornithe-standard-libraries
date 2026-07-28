package net.ornithemc.osl.items.impl.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.ornithemc.osl.items.api.ItemRegistry;

public final class ItemUtil {

	public static int itemId(Block block) {
		Item item = ItemRegistry.getItem(block);
		return item == null ? -1 : ItemRegistry.getId(item);
	}
}
