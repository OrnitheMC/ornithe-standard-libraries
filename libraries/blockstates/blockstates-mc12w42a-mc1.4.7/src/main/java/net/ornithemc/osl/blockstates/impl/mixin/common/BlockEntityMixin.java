package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.blockstates.api.block.entity.BlockEntityExtension;
import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.core.api.util.math.BlockPos;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements BlockEntityExtension {

	@Shadow
	private int x;
	@Shadow
	private int y;
	@Shadow
	private int z;

	@Shadow
	private Block getBlock() { return null; }
	@Shadow
	private int getBlockMetadata() { return 0; }

	@Override
	public BlockPos getPos() {
		return BlockPos.pooled(this.x, this.y, this.z);
	}

	@Override
	public void setPos(BlockPos pos) {
		this.x = pos.x();
		this.y = pos.y();
		this.z = pos.z();
	}

	@Override
	public BlockState getBlockState() {
		Block block = this.getBlock();
		int metadata = this.getBlockMetadata();

		return block.getStateFromMetadata(metadata);
	}
}
