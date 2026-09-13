package net.ornithemc.osl.blockstates.impl.block.entity;

import net.ornithemc.osl.blockstates.api.block.entity.BlockEntityExtension;
import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.core.api.util.math.BlockPos;

public interface BlockEntityExtensionImpl extends BlockEntityExtension {

	@Override
	default BlockPos getPos() {
		throw new AbstractMethodError();
	}

	@Override
	default void setPos(BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState getBlockState() {
		throw new AbstractMethodError();
	}
}
