package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;

import net.ornithemc.osl.blockstates.api.block.state.BlockStateExtension;

@Mixin(BlockState.class)
public class BlockStateMixin implements BlockStateExtension {

	@Shadow
	private Block getBlock() { return null; }

	@Override
	public boolean is(Block block) {
		return this.getBlock().is(block);
	}
}
