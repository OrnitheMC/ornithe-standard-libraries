package net.ornithemc.osl.blockstates.api.block.entity;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.core.api.util.math.BlockPos;

public interface BlockEntityExtension {

	BlockPos getPos();

	void setPos(BlockPos pos);

	BlockState getBlockState();

}
