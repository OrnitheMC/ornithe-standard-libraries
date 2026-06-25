package net.ornithemc.osl.blocks.impl.block;

import net.ornithemc.osl.blocks.api.block.BlockExtension;

public interface BlockExtensionImpl extends BlockExtension {

	@Override
	default boolean isAir() {
		throw new AbstractMethodError();
	}
}
