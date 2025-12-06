package net.ornithemc.osl.core.api.util;

import net.ornithemc.osl.core.impl.util.NamespacedIdentifierException;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierParseException;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierImpl;

/**
 * Utility methods for creating and validating {@link NamespacedIdentifier}s.
 */
public final class NamespacedIdentifiers {

	/**
	 * The {@code minecraft} namespace is used for Vanilla resources and ids.
	 */
	public static final String MINECRAFT_NAMESPACE = "minecraft";
	/**
	 * The default namespace of {@code NamespacedIdentifier}s.
	 * It is recommended to use a custom namespace for your own identifiers.
	 */
	public static final String DEFAULT_NAMESPACE = MINECRAFT_NAMESPACE;

	/**
	 * The maximum length of a {@code NamespacedIdentifier}'s namespace string.
	 */
	public static final int MAX_LENGTH_NAMESPACE = Integer.MAX_VALUE;
	/**
	 * The maximum length of a {@code NamespacedIdentifier} identifier string.
	 */
	public static final int MAX_LENGTH_IDENTIFIER = Integer.MAX_VALUE;

	/**
	 * Construct and validate a {@code NamespacedIdentifier} with the default namespace and the given identifier.
	 * 
	 * @return a {@code NamespacedIdentifier} with the default namespace and the given identifier.
	 * @throws NamespacedIdentifierException
	 *   if the given identifier is invalid.
	 */
	public static NamespacedIdentifier from(String identifier) {
		return from(DEFAULT_NAMESPACE, identifier);
	}

	/**
	 * Construct and validate a {@code NamespacedIdentifier} from the given namespace and identifier.
	 * 
	 * @return a {@code NamespacedIdentifier} with the given namespace and identifier.
	 * @throws NamespacedIdentifierException
	 *   if the given namespace or identifier is invalid.
	 */
	public static NamespacedIdentifier from(String namespace, String identifier) {
		return new NamespacedIdentifierImpl(
			validateNamespace(namespace),
			validateIdentifier(identifier)
		);
	}

	/**
	 * Parse a {@code NamespacedIdentifier} from the given {@code String}.
	 * The returned identifier is always valid. If no valid identifier can
	 * be parsed from the given string, an exception is thrown.
	 * 
	 * @return the {@code NamespacedIdentifier}} represented by the {@code String}.
	 * @throws NamespacedIdentifierParseException
	 *   if no valid {@code NamespacedIdentifier} can be parsed from the given {@code String}.
	 */
	public static NamespacedIdentifier parse(String s) {
		int i = s.indexOf(NamespacedIdentifier.SEPARATOR);

		try {
			if (i < 0) {
				return from(s.substring(i + 1));
			} else if (i > 0) {
				return from(s.substring(0, i), s.substring(i + 1));
			} else {
				throw NamespacedIdentifierParseException.invalid(s, "badly formatted");
			}
		} catch (NamespacedIdentifierException e) {
			throw NamespacedIdentifierParseException.invalid(s, e);
		}
	}

	/**
	 * Check whether the given {@code NamespacedIdentifier} is valid, or throw an exception.
	 */
	public static NamespacedIdentifier validate(NamespacedIdentifier id) {
		try {
			validateNamespace(id.namespace());
			validateIdentifier(id.identifier());

			return id;
		} catch (NamespacedIdentifierException e) {
			throw NamespacedIdentifierException.invalid(id, e);
		}
	}

	/**
	 * Check that the given namespace is valid for a {@code NamespacedIdentifier}.
	 */
	public static String validateNamespace(String namespace) {
		if (namespace == null || namespace.isEmpty()) {
			throw NamespacedIdentifierException.invalidNamespace(namespace, "null or empty");
		}
		if (namespace.length() > MAX_LENGTH_NAMESPACE) {
			throw NamespacedIdentifierException.invalidNamespace(namespace, "length " + namespace.length() + " is greater than maximum allowed " + MAX_LENGTH_NAMESPACE);
		}
		if (!namespace.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || (chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z') || (chr >= '0' && chr <= '9'))) {
			throw NamespacedIdentifierException.invalidNamespace(namespace, "contains illegal characters - only [a-zA-Z0-9-._] are allowed");
		}

		return namespace;
	}

	/**
	 * Check that the given identifier is valid for a {@code NamespacedIdentifier}.
	 */
	public static String validateIdentifier(String identifier) {
		if (identifier == null || identifier.isEmpty()) {
			throw NamespacedIdentifierException.invalidIdentifier(identifier, "null or empty");
		}
		if (identifier.length() > MAX_LENGTH_IDENTIFIER) {
			throw NamespacedIdentifierException.invalidIdentifier(identifier, "length " + identifier.length() + " is greater than maximum allowed " + MAX_LENGTH_IDENTIFIER);
		}
		if (!identifier.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z') || (chr >= '0' && chr <= '9'))) {
			throw NamespacedIdentifierException.invalidIdentifier(identifier, "contains illegal characters - only [a-zA-Z0-9-._/] are allowed");
		}

		return identifier;
	}

	public static boolean equals(NamespacedIdentifier a, NamespacedIdentifier b) {
		return a.namespace().equals(b.namespace()) && a.identifier().equals(b.identifier());
	}
}
