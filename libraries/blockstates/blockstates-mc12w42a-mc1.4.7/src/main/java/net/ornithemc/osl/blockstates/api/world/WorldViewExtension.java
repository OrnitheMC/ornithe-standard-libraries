package net.ornithemc.osl.blockstates.api.world;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.core.api.util.math.BlockPos;

public interface WorldViewExtension {

	int WORLD_MIN_Y = 0;
	int WORLD_MAX_Y = 255;

	BlockState getBlockState(BlockPos pos);

	BlockState getBlockState(int x, int y, int z);

	static boolean isInsideWorldHeight(BlockPos pos) {
		return isInsideWorldHeight(pos.y());
	}

	static boolean isInsideWorldHeight(int y) {
		return y >= WORLD_MIN_Y && y <= WORLD_MAX_Y;
	}
}
