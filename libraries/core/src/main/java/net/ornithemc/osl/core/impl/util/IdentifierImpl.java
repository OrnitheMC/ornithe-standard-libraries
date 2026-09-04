package net.ornithemc.osl.core.impl.util;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

// This interface is used for transitive interface injection into Identifier.
// Its purpose is to provide method implementations to keep the compiler happy.
public interface IdentifierImpl extends NamespacedIdentifier {
	@Override
	default String namespace() {
		throw new AbstractMethodError("Not implemented!");
	}

	@Override
	default String path() {
		throw new AbstractMethodError("Not implemented!");
	}
}
