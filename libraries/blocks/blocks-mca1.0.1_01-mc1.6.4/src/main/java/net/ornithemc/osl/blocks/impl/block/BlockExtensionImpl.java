package net.ornithemc.osl.blocks.impl.block;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.block.BlockExtension;

public interface BlockExtensionImpl extends BlockExtension {

	@Override
	default boolean isAir() {
		throw new AbstractMethodError();
	}

	@Override
	default boolean is(Block block) {
		throw new AbstractMethodError();
	}
}
