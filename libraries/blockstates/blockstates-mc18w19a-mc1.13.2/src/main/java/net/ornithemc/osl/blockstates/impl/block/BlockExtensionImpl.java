package net.ornithemc.osl.blockstates.impl.block;

import net.minecraft.block.Block;

import net.ornithemc.osl.blockstates.api.block.BlockExtension;

public interface BlockExtensionImpl extends BlockExtension {

	@Override
	default boolean is(Block block) {
		throw new AbstractMethodError();
	}
}
