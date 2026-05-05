package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.block.BlockExtension;

@Mixin(Block.class)
public class BlockMixinOld implements BlockExtension {

	@Override
	public boolean isAir() {
		return false;
	}

	@Override
	public boolean is(Block block) {
		return (Block) (Object) this == block;
	}
}
