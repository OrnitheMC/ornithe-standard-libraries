package net.ornithemc.osl.commands.impl.block.state;

import com.google.common.base.Optional;

public interface PropertyExtensionImpl {

	default Optional<Comparable<?>> getValue(String name) {
		throw new AbstractMethodError();
	}
}
