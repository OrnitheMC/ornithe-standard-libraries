package net.ornithemc.osl.core.api.util;

import java.util.Objects;

/**
 * Namespaced identifiers are two-part strings that uniquely point to content in Minecraft.
 * The two parts are the namespace and the identifier. They can be combined into a single
 * string representation as namespace:identifier (the namespace, followed by the identifier,
 * separated by a colon).
 * <p>
 * A namespace is a domain for content. It is used not to point to specific content, but to
 * differentiate between different content sources or publishers. The use of namespaces can
 * prevent conflicts between mods, resource packs, or data packs, in cases where the same
 * identifier is used.
 * <p>
 * The identifier is a unique name for content within a namespace. It should be descriptive
 * to avoid naming conflicts with other content. The preferred format is snake_case.
 * <p>
 * Namespaces may only contain alphanumeric characters [a-zA-Z0-9] and special characters
 * [-._]. Identifiers may also contain the special character [/].
 * <p>
 * This class is essentially equivalent to Vanilla's {@code Identifier}. It was added for a
 * few reasons. For one, Vanilla's {@code Identifier} was only added in 13w21a, and then was
 * client-only until 14w27b. Implementation details of this class also changed a few times,
 * and only since 17w43a were {@code Identifiers} validated in any way.
 * <br> This class is available for all Minecraft versions, without any version-specific
 * implementation details.
 */
public final class NamespacedIdentifier {

	/**
	 * The separator between the namespace and identifier in the {@code String}
	 * representation of a {@code NamespacedIdentifier}.
	 */
	public static final char SEPARATOR = ':';

	/**
	 * The namespace of this {@code NamespacedIdentifier}.
	 */
	private final String namespace;
	/**
	 * The identifier of this {@code NamespacedIdentifier}.
	 */
	private final String identifier;

	NamespacedIdentifier(String namespace, String identifier) {
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
		return namespace + SEPARATOR + identifier;
	}

	/**
	 * @return the namespace of this {@code NamespacedIdentifier}.
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @return the identifier of this {@code NamespacedIdentifier}.
	 */
	public String getIdentifier() {
		return identifier;
	}
}
