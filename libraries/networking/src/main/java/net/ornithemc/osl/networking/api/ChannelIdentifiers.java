package net.ornithemc.osl.networking.api;

import java.util.Set;
import java.util.stream.Collectors;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierException;
import net.ornithemc.osl.networking.impl.ChannelIdentifierException;

/**
 * Utility methods for creating and validating channel identifiers.
 */
public final class ChannelIdentifiers {
	/**
	 * The default namespace of channel identifiers.
	 * It is recommended to use a custom namespace for your own identifiers.
	 */
	public static final String DEFAULT_NAMESPACE = NamespacedIdentifier.VANILLA_NAMESPACE;

	/**
	 * The maximum length of a channel identifier's namespace string.
	 */
	public static final int MAX_LENGTH_NAMESPACE = Byte.MAX_VALUE;
	/**
	 * The maximum length of a channel identifier's identifier string.
	 */
	public static final int MAX_LENGTH_PATH = Byte.MAX_VALUE;

	/**
	 * Construct and validate a channel identifier with the default namespace and the given path.
	 */
	public static NamespacedIdentifier from(String path) {
		return from(DEFAULT_NAMESPACE, path);
	}

	/**
	 * Construct and validate a channel identifier from the given namespace and path.
	 */
	public static NamespacedIdentifier from(String namespace, String path) {
		return NamespacedIdentifiers.from(validateNamespace(namespace), validatePath(path));
	}

	/**
	 * Check whether the given channel identifier is valid, or throw an exception.
	 */
	public static NamespacedIdentifier validate(NamespacedIdentifier id) {
		try {
			validateNamespace(id.namespace());
			validatePath(id.path());
			return id;
		} catch (ChannelIdentifierException exception) {
			throw ChannelIdentifierException.invalid(id, exception);
		}
	}

	/**
	 * Check that the given namespace is valid for a channel identifier.
	 */
	public static String validateNamespace(String namespace) {
		if (namespace == null || namespace.isEmpty()) {
			throw ChannelIdentifierException.invalidNamespace(namespace, "null or empty");
		}

		if (namespace.length() > MAX_LENGTH_NAMESPACE) {
			throw ChannelIdentifierException.invalidNamespace(namespace, "length " + namespace.length() + " is greater than maximum allowed " + MAX_LENGTH_NAMESPACE);
		}

		if (!namespace.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9'))) {
			throw ChannelIdentifierException.invalidNamespace(namespace, "contains illegal characters - only [a-z0-9-._] are allowed");
		}

		return NamespacedIdentifiers.validateNamespace(namespace);
	}

	/**
	 * Check that the given path is valid for a channel identifier.
	 */
	public static String validatePath(String path) {
		if (path == null || path.isEmpty()) {
			throw ChannelIdentifierException.invalidPath(path, "null or empty");
		}

		if (path.length() > MAX_LENGTH_PATH) {
			throw ChannelIdentifierException.invalidPath(path, "length " + path.length() + " is greater than maximum allowed " + MAX_LENGTH_PATH);
		}

		if (!path.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9'))) {
			throw ChannelIdentifierException.invalidPath(path, "contains illegal characters - only [a-z0-9-._/] are allowed");
		}

		return NamespacedIdentifiers.validatePath(path);
	}

	public static Set<NamespacedIdentifier> dropInvalid(Set<NamespacedIdentifier> channels) {
		return channels
			.stream()
			.filter(channel -> {
				try {
					return ChannelIdentifiers.validate(channel) != null;
				} catch (ChannelIdentifierException | NamespacedIdentifierException e) {
					return false;
				}
			})
			.collect(Collectors.toSet());
	}
}
