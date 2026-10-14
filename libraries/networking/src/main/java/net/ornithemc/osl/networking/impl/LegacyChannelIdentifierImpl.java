package net.ornithemc.osl.networking.impl;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.networking.api.LegacyChannelIdentifier;

public final class LegacyChannelIdentifierImpl implements LegacyChannelIdentifier {

	private final String namespace;
	private final String identifier;

	public LegacyChannelIdentifierImpl(String namespace, String identifier) {
		this.namespace = namespace;
		this.identifier = identifier;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LegacyChannelIdentifier)) {
			return false;
		}
		return NamespacedIdentifiers.equals(this, (LegacyChannelIdentifier) o);
	}

	@Override
	public int hashCode() {
		return 31 * namespace.hashCode() + identifier.hashCode();
	}

	@Override
	public String toString() {
		return namespace.isEmpty() ? identifier : (namespace + SEPARATOR + identifier);
	}

	@Override
	public String namespace() {
		return namespace;
	}

	@Override
	public String identifier() {
		return identifier;
	}

	@Override
	public LegacyChannelIdentifier prefixed(String prefix) {
		return new LegacyChannelIdentifierImpl(namespace, prefix + identifier);
	}

	@Override
	public LegacyChannelIdentifier suffixed(String suffix) {
		return new LegacyChannelIdentifierImpl(namespace, identifier + suffix);
	}
}
