package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

import net.ornithemc.osl.items.impl.access.BlockItemAccess;

@Mixin(BlockItem.class)
public class BlockItemMixinNew implements BlockItemAccess {

	@Shadow
	private int block;

	@Override
	public int osl$items$getBlock() {
		return this.block;
	}

	@Override
	public void osl$items$setBlock(Block block) {
		this.block = block.id;
	}
}
