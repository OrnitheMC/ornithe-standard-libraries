package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.state.BlockState;

import net.ornithemc.osl.blockstates.api.block.entity.BlockEntityExtension;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements BlockEntityExtension {

	@Shadow
	private Block getBlock() { return null; }
	@Shadow
	private int getBlockMetadata() { return 0; }

	@Override
	public BlockState getBlockState() {
		Block block = this.getBlock();
		int metadata = this.getBlockMetadata();

		return block.getStateFromMetadata(metadata);
	}
}
