package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.WorldChunk;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.world.WorldChunkExtension;
import net.ornithemc.osl.core.api.util.math.BlockPos;

@Mixin(WorldChunk.class)
public class WorldChunkMixin implements WorldChunkExtension {

	@Shadow
	private Block getBlockAt(int localX, int y, int localZ) { return null; }
	@Shadow
	private int getBlockMetadataAt(int localX, int y, int localZ) { return 0; }
	@Shadow
	private boolean setBlockWithMetadataAt(int localX, int y, int localZ, Block block, int metadata) { return false; }

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return this.getBlockState(pos.x(), pos.y(), pos.z());
	}

	@Override
	public BlockState getBlockState(int x, int y, int z) {
		int localX = x & 0xF;
		int localZ = z & 0xF;

		Block block = this.getBlockAt(localX, y, localZ);
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

		if (this.setBlockWithMetadataAt(localX, y, localZ, block, metadata)) {
			return oldState;
		} else {
			return null;
		}
	}
}
