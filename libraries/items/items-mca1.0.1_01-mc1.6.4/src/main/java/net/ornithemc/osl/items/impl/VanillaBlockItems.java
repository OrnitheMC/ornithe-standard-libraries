package net.ornithemc.osl.items.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.ornithemc.osl.blocks.api.block.Blocks;
import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import net.ornithemc.osl.blocks.impl.VanillaBlocks;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.impl.item.ItemUtil;

public final class VanillaBlockItems {

	public static final int MAX_ID = VanillaBlocks.MAX_ID;

	static void init() {
		for (Field f : Block.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers()) && Block.class.isAssignableFrom(f.getType())) {
				try {
					Block block = (Block) f.get(null);
					Item item = Item.BY_ID[block.id];

					if (block != Blocks.AIR && item != null) {
						NamespacedIdentifier identifier = BlockRegistryImpl.getIdentifier(block);

						if (ItemRegistryImpl.getItem(identifier) != null) {
							// some blocks have both a block item and special item form
							identifier = identifier.suffixed("_block");
						}

						ItemRegistryImpl.register(identifier, block, item);
					}
				} catch (Throwable t) {
				}
			}
		}

		ItemUtil.blockItemsInitialized = true;
	}
}
