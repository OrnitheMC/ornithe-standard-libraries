package net.ornithemc.osl.blockstates.impl.block;

import net.ornithemc.osl.blockstates.api.block.BlockExtension;

public interface BlockExtensionImpl extends BlockExtension {

	@Override
	default boolean isAir() {
		throw new AbstractMethodError();
	}
}
