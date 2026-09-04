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
	private final String path;

	public NamespacedIdentifierImpl(String namespace, String path) {
		this.namespace = namespace;
		this.path = path;
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
		return 31 * namespace.hashCode() + path.hashCode();
	}

	@Override
	public String toString() {
		return namespace + SEPARATOR + path;
	}

	@Override
	public String namespace() {
		return namespace;
	}

	@Override
	public String path() {
		return path;
	}
}
