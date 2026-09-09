package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.Block;

import net.ornithemc.osl.blockstates.api.block.BlockExtension;

@Mixin(Block.class)
public class BlockMixin implements BlockExtension {

	@Override
	public boolean is(Block block) {
		return this == (Object) block;
	}
}
