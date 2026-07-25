package net.ornithemc.osl.core.impl.util;

import net.minecraft.client.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface IdentifierExtensionImpl extends NamespacedIdentifier {

	@Override
	default String namespace() {
		throw new AbstractMethodError();
	}

	@Override
	default String identifier() {
		throw new AbstractMethodError();
	}

	@Override
	default Identifier prefixed(String prefix) {
		throw new AbstractMethodError();
	}

	@Override
	default Identifier suffixed(String suffix) {
		throw new AbstractMethodError();
	}
}
