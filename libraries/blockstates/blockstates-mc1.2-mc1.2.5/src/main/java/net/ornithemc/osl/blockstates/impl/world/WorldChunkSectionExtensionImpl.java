package net.ornithemc.osl.blockstates.impl.world;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.world.WorldChunkSectionExtension;

public interface WorldChunkSectionExtensionImpl extends WorldChunkSectionExtension {

	@Override
	default BlockState getBlockState(int x, int y, int z) {
		throw new AbstractMethodError();
	}

	@Override
	default void setBlockState(int x, int y, int z, BlockState state) {
		throw new AbstractMethodError();
	}
}
