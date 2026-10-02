package net.ornithemc.osl.blockstates.impl.block.state;

import net.minecraft.block.Block;

import net.ornithemc.osl.blockstates.api.block.state.BlockStateExtension;

public interface BlockStateExtensionImpl extends BlockStateExtension {

	@Override
	default boolean is(Block block) {
		throw new AbstractMethodError();
	}
}
