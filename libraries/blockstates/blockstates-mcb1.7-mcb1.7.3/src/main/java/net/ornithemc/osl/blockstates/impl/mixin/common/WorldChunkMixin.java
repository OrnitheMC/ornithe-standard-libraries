package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.chunk.WorldChunk;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.world.WorldChunkExtension;
import net.ornithemc.osl.core.api.util.math.BlockPos;

@Mixin(WorldChunk.class)
public class WorldChunkMixin implements WorldChunkExtension {

	@Shadow
	private int getBlockAt(int localX, int y, int localZ) { return 0; }
	@Shadow
	private int getBlockMetadataAt(int localX, int y, int localZ) { return 0; }
	@Shadow
	private boolean setBlockWithMetadataAt(int localX, int y, int localZ, int block, int metadata) { return false; }
	@Shadow
	private BlockEntity getBlockEntityAt(int localX, int y, int localZ) { return null; }
	@Shadow
	private void setBlockEntityAt(int localX, int y, int localZ, BlockEntity blockEntity) { }

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return this.getBlockState(pos.x(), pos.y(), pos.z());
	}

	@Override
	public BlockState getBlockState(int x, int y, int z) {
		int localX = x & 0xF;
		int localZ = z & 0xF;

		Block block = Block.BY_ID[this.getBlockAt(localX, y, localZ)];
		int metadata = this.getBlockMetadataAt(localX, y, localZ);

		return block.getStateFromMetadata(metadata);
	}

	@Override
	public BlockState setBlockState(BlockPos pos, BlockState state) {
		return this.setBlockState(pos.x(), pos.y(), pos.z(), state);
	}

	@Override
	public BlockState setBlockState(int x, int y, int z, BlockState state) {
		BlockState oldState = this.getBlockState(x, y, z);

		int localX = x & 0xF;
		int localZ = z & 0xF;

		Block block = state.getBlock();
		int metadata = block.getMetadataFromState(state);

		if (this.setBlockWithMetadataAt(localX, y, localZ, block.id, metadata)) {
			return oldState;
		} else {
			return null;
		}
	}

	@Override
	public BlockEntity getBlockEntity(BlockPos pos) {
		int localX = pos.x() & 0xF;
		int y = pos.y();
		int localZ = pos.z() & 0xF;

		return this.getBlockEntityAt(localX, y, localZ);
	}

	@Override
	public void setBlockEntity(BlockPos pos, BlockEntity blockEntity) {
		int localX = pos.x() & 0xF;
		int y = pos.y();
		int localZ = pos.z() & 0xF;

		this.setBlockEntityAt(localX, y, localZ, blockEntity);
	}
}
