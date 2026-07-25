package net.ornithemc.osl.core.api.util;

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
 */
public interface NamespacedIdentifier {

	/**
	 * The separator between the namespace and identifier in the {@code String}
	 * representation of a {@code NamespacedIdentifier}.
	 */
	static char SEPARATOR = ':';

	/**
	 * @return the namespace of this {@code NamespacedIdentifier}.
	 */
	String namespace();

	/**
	 * @return the identifier of this {@code NamespacedIdentifier}.
	 */
	String identifier();

	/**
	 * @return a copy of this {@code NamespacedIdentifier} with the given prefix.
	 */
	NamespacedIdentifier prefixed(String prefix);

	/**
	 * @return a copy of this {@code NamespacedIdentifier} with the given suffix.
	 */
	NamespacedIdentifier suffixed(String suffix);

}
