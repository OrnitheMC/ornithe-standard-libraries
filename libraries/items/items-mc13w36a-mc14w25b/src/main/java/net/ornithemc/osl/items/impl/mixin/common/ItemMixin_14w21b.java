package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.ornithemc.osl.items.impl.ItemRegistryImpl;

@Mixin(Item.class)
public class ItemMixin_14w21b {

	@Overwrite
	private static Item byBlock(Block block) {
		return ItemRegistryImpl.BLOCK_ITEMS.get(block);
	}
}
