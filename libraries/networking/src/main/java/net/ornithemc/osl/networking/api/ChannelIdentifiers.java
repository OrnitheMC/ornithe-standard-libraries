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
	public static final String DEFAULT_NAMESPACE = NamespacedIdentifiers.DEFAULT_NAMESPACE;

	/**
	 * The maximum length of a channel identifier's namespace string.
	 */
	public static final int MAX_LENGTH_NAMESPACE = Byte.MAX_VALUE;
	/**
	 * The maximum length of a channel identifier's identifier string.
	 */
	public static final int MAX_LENGTH_IDENTIFIER = Byte.MAX_VALUE;

	/**
	 * Construct and validate a channel identifier with the default namespace and the given identifier.
	 */
	public static NamespacedIdentifier from(String identifier) {
		return from(DEFAULT_NAMESPACE, identifier);
	}

	/**
	 * Construct and validate a channel identifier from the given namespace and identifier.
	 */
	public static NamespacedIdentifier from(String namespace, String identifier) {
		return NamespacedIdentifiers.from(
			validateNamespace(namespace),
			validateIdentifier(identifier)
		);
	}

	/**
	 * Check whether the given channel identifier is valid, or throw an exception.
	 */
	public static NamespacedIdentifier validate(NamespacedIdentifier id) {
		try {
			validateNamespace(id.namespace());
			validateIdentifier(id.identifier());

			return id;
		} catch (ChannelIdentifierException e) {
			throw ChannelIdentifierException.invalid(id, e);
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
	 * Check that the given identifier is valid for a channel identifier.
	 */
	public static String validateIdentifier(String identifier) {
		if (identifier == null || identifier.isEmpty()) {
			throw ChannelIdentifierException.invalidIdentifier(identifier, "null or empty");
		}
		if (identifier.length() > MAX_LENGTH_IDENTIFIER) {
			throw ChannelIdentifierException.invalidIdentifier(identifier, "length " + identifier.length() + " is greater than maximum allowed " + MAX_LENGTH_IDENTIFIER);
		}
		if (!identifier.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9'))) {
			throw ChannelIdentifierException.invalidIdentifier(identifier, "contains illegal characters - only [a-z0-9-._/] are allowed");
		}

		return NamespacedIdentifiers.validateIdentifier(identifier);
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
