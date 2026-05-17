package net.ornithemc.osl.blockstates.impl.world;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.world.WorldViewExtension;
import net.ornithemc.osl.core.api.util.math.BlockPos;

public interface WorldViewExtensionImpl extends WorldViewExtension {

	@Override
	default BlockState getBlockState(BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState getBlockState(int x, int y, int z) {
		throw new AbstractMethodError();
	}
}
