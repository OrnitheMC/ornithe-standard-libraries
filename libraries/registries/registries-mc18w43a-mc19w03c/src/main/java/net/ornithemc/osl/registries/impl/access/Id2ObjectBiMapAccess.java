package net.ornithemc.osl.registries.impl.access;

import it.unimi.dsi.fastutil.ints.IntSet;

public interface Id2ObjectBiMapAccess {

	default IntSet osl$registries$idSet() {
		throw new AbstractMethodError();
	}

	default void osl$registries$clear() {
		throw new AbstractMethodError();
	}
}
