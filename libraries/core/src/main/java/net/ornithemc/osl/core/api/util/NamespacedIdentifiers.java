package net.ornithemc.osl.core.api.util;

import java.util.Comparator;

import net.ornithemc.osl.core.impl.util.NamespacedIdentifierException;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierParseException;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierImpl;

/**
 * Utility methods for creating and validating {@link NamespacedIdentifier}s.
 */
public final class NamespacedIdentifiers {
	/**
	 * The maximum length of a {@code NamespacedIdentifier}'s namespace string.
	 */
	public static final int MAX_LENGTH_NAMESPACE = Integer.MAX_VALUE;

	/**
	 * The maximum length of a {@code NamespacedIdentifier} path string.
	 */
	public static final int MAX_LENGTH_PATH = Integer.MAX_VALUE;

	/**
	 * A comparator for {@code NamespacedIdentifier}s, comparing first by path, then by namespace.
	 */
	public static final Comparator<NamespacedIdentifier> COMPARATOR = Comparator.comparing(NamespacedIdentifier::path).thenComparing(NamespacedIdentifier::namespace);

	/**
	 * Construct and validate a {@code NamespacedIdentifier} with the default namespace and the given path.
	 * 
	 * @return a {@code NamespacedIdentifier} with the default namespace and the given path.
	 * @throws NamespacedIdentifierException
	 *   if the given identifier is invalid.
	 */
	public static NamespacedIdentifier from(String path) {
		return from(NamespacedIdentifier.VANILLA_NAMESPACE, path);
	}

	/**
	 * Construct and validate a {@code NamespacedIdentifier} from the given namespace and path.
	 * 
	 * @return a {@code NamespacedIdentifier} with the given namespace and path.
	 * @throws NamespacedIdentifierException
	 *   if the given namespace or identifier is invalid.
	 */
	public static NamespacedIdentifier from(String namespace, String path) {
		return new NamespacedIdentifierImpl(validateNamespace(namespace), validatePath(path));
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
	public static NamespacedIdentifier parse(String input) {
		int index = input.indexOf(NamespacedIdentifier.SEPARATOR);
		try {
			if (index < 0) {
				return from(input.substring(index + 1));
			} else if (index > 0) {
				return from(input.substring(0, index), input.substring(index + 1));
			} else {
				throw NamespacedIdentifierParseException.invalid(input, "badly formatted");
			}
		} catch (NamespacedIdentifierException exception) {
			throw NamespacedIdentifierParseException.invalid(input, exception);
		}
	}

	/**
	 * Check whether the given {@code NamespacedIdentifier} is valid, or throw an exception.
	 */
	public static NamespacedIdentifier validate(NamespacedIdentifier id) {
		try {
			validateNamespace(id.namespace());
			validatePath(id.path());
			return id;
		} catch (NamespacedIdentifierException exception) {
			throw NamespacedIdentifierException.invalid(id, exception);
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
	 * Check that the given path is valid for a {@code NamespacedIdentifier}.
	 */
	public static String validatePath(String path) {
		if (path == null || path.isEmpty()) {
			throw NamespacedIdentifierException.invalidPath(path, "null or empty");
		}

		if (path.length() > MAX_LENGTH_PATH) {
			throw NamespacedIdentifierException.invalidPath(path, "length " + path.length() + " is greater than maximum allowed " + MAX_LENGTH_PATH);
		}

		if (!path.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z') || (chr >= '0' && chr <= '9'))) {
			throw NamespacedIdentifierException.invalidPath(path, "contains illegal characters - only [a-zA-Z0-9-._/] are allowed");
		}

		return path;
	}

	public static boolean equals(NamespacedIdentifier left, NamespacedIdentifier right) {
		return left.namespace().equals(right.namespace()) && left.path().equals(right.path());
	}
}
