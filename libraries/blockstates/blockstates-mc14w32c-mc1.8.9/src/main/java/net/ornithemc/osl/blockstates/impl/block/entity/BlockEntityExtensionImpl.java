package net.ornithemc.osl.blockstates.impl.block.entity;

import net.minecraft.block.state.BlockState;

import net.ornithemc.osl.blockstates.api.block.entity.BlockEntityExtension;

public interface BlockEntityExtensionImpl extends BlockEntityExtension {

	@Override
	default BlockState getBlockState() {
		throw new AbstractMethodError();
	}
}
