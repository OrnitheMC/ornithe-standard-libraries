package net.ornithemc.osl.blockstates.api.world;

import net.minecraft.world.chunk.WorldChunk;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.core.api.util.math.BlockPos;

public interface WorldExtension extends WorldViewExtension {

	int WORLD_RADIUS = 30000000;

	WorldChunk getChunk(BlockPos pos);

	boolean setBlockState(BlockPos pos, BlockState state, int flags);

	boolean setBlockState(int x, int y, int z, BlockState state, int flags);

	static boolean isInsideWorld(BlockPos pos) {
		return isInsideWorld(pos.x(), pos.y(), pos.z());
	}

	static boolean isInsideWorld(int x, int y, int z) {
		return x >= -WORLD_RADIUS && x < WORLD_RADIUS && z >= -WORLD_RADIUS && z < WORLD_RADIUS && y >= WORLD_MIN_Y && y <= WORLD_MAX_Y;
	}
}
