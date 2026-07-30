package net.ornithemc.osl.items.impl.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.ornithemc.osl.items.api.ItemRegistry;
import net.ornithemc.osl.items.impl.mixin.common.BlockItemAccess;

public final class ItemUtil {

	public static boolean blockItemsInitialized;

	public static int blockId(ItemStack item) {
		return item.getItem() instanceof BlockItem ? ((BlockItemAccess) item.getItem()).accessBlock() : 0;
	}

	public static int itemId(int block) {
		return itemId(Block.BY_ID[block]);
	}

	public static int itemId(Block block) {
		Item item = ItemRegistry.getItem(block);
		return item == null ? 0 : item.id;
	}
}
