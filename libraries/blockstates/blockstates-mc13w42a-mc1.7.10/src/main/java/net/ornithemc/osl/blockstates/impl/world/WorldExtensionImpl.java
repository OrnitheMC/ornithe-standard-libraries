package net.ornithemc.osl.blockstates.impl.world;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.world.WorldExtension;
import net.ornithemc.osl.core.api.util.math.BlockPos;

public interface WorldExtensionImpl extends WorldViewExtensionImpl, WorldExtension {

	@Override
	default boolean setBlockState(BlockPos pos, BlockState state, int flags) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean setBlockState(int x, int y, int z, BlockState state, int flags) {
		throw new AbstractMethodError();
	}
}
