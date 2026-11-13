package net.ornithemc.osl.blockstates.impl.block.state;

import com.google.common.base.Optional;

import net.ornithemc.osl.blockstates.api.block.state.PropertyExtension;

public interface PropertyExtensionImpl extends PropertyExtension {

	@Override
	default Optional<Comparable<?>> getValue(String name) {
		throw new AbstractMethodError();
	}
}
