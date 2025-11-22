package net.ornithemc.osl.core.api.util;

import java.util.Objects;

public final class NamespacedIdentifier {

	public static NamespacedIdentifier fromMinecraft(String identifier) {
		return new NamespacedIdentifier("minecraft", identifier);
	}

	public static NamespacedIdentifier from(String namespace, String identifier) {
		return new NamespacedIdentifier(namespace, identifier);
	}

	private final String namespace;
	private final String identifier;

	private NamespacedIdentifier(String namespace, String identifier) {
		this.namespace = namespace;
		this.identifier = identifier;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof NamespacedIdentifier)) {
			return false;
		}
		NamespacedIdentifier id = (NamespacedIdentifier) o;
		return namespace.equals(id.namespace) && identifier.equals(id.identifier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, identifier);
	}

	@Override
	public String toString() {
		return namespace + ":" + identifier;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getIdentifier() {
		return identifier;
	}
}
