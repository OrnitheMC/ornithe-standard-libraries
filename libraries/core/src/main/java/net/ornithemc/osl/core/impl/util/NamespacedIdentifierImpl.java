package net.ornithemc.osl.core.impl.util;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

/**
 * This class is a version-agnostic implementation of {@link NamespacedIdentifier}.
 * <p>
 * This class is essentially equivalent to Vanilla's {@code Identifier}. It was added for a
 * few reasons. For one, Vanilla's {@code Identifier} was only added in 13w21a, and then was
 * client-only until 14w27b. Implementation details of this class also changed a few times,
 * and only since 17w43a were {@code Identifiers} validated in any way.
 * <br> This class is available for all Minecraft versions, without any version-specific
 * implementation details.
 */
public final class NamespacedIdentifierImpl implements NamespacedIdentifier {

	private final String namespace;
	private final String identifier;

	public NamespacedIdentifierImpl(String namespace, String identifier) {
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
		return NamespacedIdentifiers.equals(this, (NamespacedIdentifier) o);
	}

	@Override
	public int hashCode() {
		// this impl matches Vanilla Identifier's impl
		return 31 * namespace.hashCode() + identifier.hashCode();
	}

	@Override
	public String toString() {
		return namespace + SEPARATOR + identifier;
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
	public NamespacedIdentifier prefixed(String prefix) {
		return new NamespacedIdentifierImpl(namespace, prefix + identifier);
	}

	@Override
	public NamespacedIdentifier suffixed(String suffix) {
		return new NamespacedIdentifierImpl(namespace, identifier + suffix);
	}
}
