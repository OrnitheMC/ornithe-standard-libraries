package net.ornithemc.osl.items.impl;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.ornithemc.osl.blocks.api.block.Blocks;
import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

final class VanillaBlockItems {

	static void init() {
		for (int id = 0; id < VanillaItems.ITEM_ID_OFFSET; id++) {
			Block block = Block.BY_ID[id];
			Item item = Item.BY_ID[id];

			if (block != Blocks.AIR && item != null) {
				NamespacedIdentifier key = BlockRegistryImpl.getKey(block);

				if (ItemRegistryImpl.getItem(key) == null) {
					ItemRegistryImpl.register(block, item);
				} else {
					// some blocks have both a block item and special item form
					// we should handle that in some way, but how? TODO
				}
			}
		}
	}
}
