package net.ornithemc.osl.blockstates.api.world;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.core.api.util.math.BlockPos;

public interface WorldChunkExtension {

	BlockState getBlockState(BlockPos pos);

	BlockState getBlockState(int x, int y, int z);

	BlockState setBlockState(BlockPos pos, BlockState state);

	BlockState setBlockState(int x, int y, int z, BlockState state);

}
