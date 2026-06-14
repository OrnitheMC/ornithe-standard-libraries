package net.ornithemc.osl.items.impl;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.ornithemc.osl.blocks.api.block.Blocks;

final class VanillaBlockItems {

	static void init() {
		for (int id = 0; id < VanillaItems.ITEM_ID_OFFSET; id++) {
			Block block = Block.BY_ID[id];
			Item item = Item.BY_ID[id];

			if (block != Blocks.AIR && item != null) {
				if (ItemRegistryImpl.getItem(id) == null) {
					ItemRegistryImpl.register(block, item);
				} else {
					// what the fuck?
				}
			}
		}
	}
}
