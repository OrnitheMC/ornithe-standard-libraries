package net.ornithemc.osl.networking.impl;

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
	public LegacyChannelIdentifier namespaced(String namespace) {
		return new LegacyChannelIdentifierImpl(namespace, identifier);
	}

	@Override
	public LegacyChannelIdentifier prefixed(String prefix) {
		return new LegacyChannelIdentifierImpl(namespace, prefix + identifier);
	}

	@Override
	public LegacyChannelIdentifier suffixed(String suffix) {
		return new LegacyChannelIdentifierImpl(namespace, identifier + suffix);
	}

	@Override
	public LegacyChannelIdentifier subidentifier(int beginIndex) {
		return new LegacyChannelIdentifierImpl(namespace, identifier.substring(beginIndex));
	}

	@Override
	public LegacyChannelIdentifier subidentifier(int beginIndex, int endIndex) {
		return new LegacyChannelIdentifierImpl(namespace, identifier.substring(beginIndex, endIndex));
	}
}
