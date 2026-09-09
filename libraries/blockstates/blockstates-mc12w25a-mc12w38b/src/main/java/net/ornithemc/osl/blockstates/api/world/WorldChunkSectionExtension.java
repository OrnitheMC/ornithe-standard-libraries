package net.ornithemc.osl.blockstates.api.world;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;

public interface WorldChunkSectionExtension {

	BlockState getBlockState(int x, int y, int z);

	void setBlockState(int x, int y, int z, BlockState state);

}
