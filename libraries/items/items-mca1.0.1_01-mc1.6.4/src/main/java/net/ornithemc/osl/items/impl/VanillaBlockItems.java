package net.ornithemc.osl.items.impl;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.ornithemc.osl.blocks.api.block.Blocks;
import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.impl.item.ItemUtil;

final class VanillaBlockItems {

	static void init() {
		for (int id = 0; id < VanillaItems.ITEM_ID_OFFSET; id++) {
			Block block = Block.BY_ID[id];
			Item item = Item.BY_ID[id];

			if (block != Blocks.AIR && item != null) {
				NamespacedIdentifier identifier = BlockRegistryImpl.getIdentifier(block);

				if (item.id != block.id) {
					throw new IllegalStateException("item ID must match block ID for vanilla block items! (" + identifier + " has mismatched item ID " + item.id + " and block ID " + block.id + ")");
				}

				if (ItemRegistryImpl.getItem(identifier) != null) {
					// some blocks have both a block item and special item form
					identifier = identifier.suffixed("_block");
				}

				ItemRegistryImpl.register(identifier, block, item);
			}
		}

		ItemUtil.blockItemsInitialized = true;
	}
}
