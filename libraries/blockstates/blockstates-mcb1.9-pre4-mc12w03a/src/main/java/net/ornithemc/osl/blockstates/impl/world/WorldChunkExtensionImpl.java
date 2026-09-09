package net.ornithemc.osl.blockstates.impl.world;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.world.WorldChunkExtension;
import net.ornithemc.osl.core.api.util.math.BlockPos;

public interface WorldChunkExtensionImpl extends WorldChunkExtension {

	@Override
	default BlockState getBlockState(BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState getBlockState(int x, int y, int z) {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState setBlockState(BlockPos pos, BlockState state) {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState setBlockState(int x, int y, int z, BlockState state) {
		throw new AbstractMethodError();
	}
}
