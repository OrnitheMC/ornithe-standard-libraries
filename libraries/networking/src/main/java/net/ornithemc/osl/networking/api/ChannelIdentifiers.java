package net.ornithemc.osl.networking.api;

import java.util.Set;
import java.util.stream.Collectors;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierException;
import net.ornithemc.osl.networking.impl.ChannelIdentifierException;
import net.ornithemc.osl.networking.impl.LegacyChannelIdentifierImpl;

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
	 * Construct a legacy channel identifier with the given identifier.
	 * 
	 * @see LegacyChannelIdentifier
	 */
	public static LegacyChannelIdentifier fromLegacy(String identifier) {
		return fromLegacy("", identifier);
	}

	/**
	 * Construct a legacy channel identifier with the given namespace and identifier.
	 * 
	 * @see LegacyChannelIdentifier
	 */
	public static LegacyChannelIdentifier fromLegacy(String namespace, String identifier) {
		return new LegacyChannelIdentifierImpl(namespace, identifier);
	}

	/**
	 * Check whether the given channel identifier is valid, or throw an exception.
	 */
	public static NamespacedIdentifier validate(NamespacedIdentifier id) {
		try {
			validateNamespace(id.namespace());
			validateIdentifier(id.identifier());

			return id;
		} catch (ChannelIdentifierException | NamespacedIdentifierException e) {
			throw ChannelIdentifierException.invalid(id, e);
		}
	}

	/**
	 * Check that the given namespace is valid for a channel identifier.
	 */
	public static String validateNamespace(String namespace) {
		if (namespace.length() > MAX_LENGTH_NAMESPACE) {
			throw ChannelIdentifierException.invalidNamespace(namespace, "length " + namespace.length() + " is greater than maximum allowed " + MAX_LENGTH_NAMESPACE);
		}

		return NamespacedIdentifiers.validateNamespace(namespace);
	}

	/**
	 * Check that the given identifier is valid for a channel identifier.
	 */
	public static String validateIdentifier(String identifier) {
		if (identifier.length() > MAX_LENGTH_IDENTIFIER) {
			throw ChannelIdentifierException.invalidIdentifier(identifier, "length " + identifier.length() + " is greater than maximum allowed " + MAX_LENGTH_IDENTIFIER);
		}

		return NamespacedIdentifiers.validateIdentifier(identifier);
	}

	public static Set<NamespacedIdentifier> dropInvalid(Set<NamespacedIdentifier> channels) {
		return channels
			.stream()
			.filter(channel -> {
				try {
					return ChannelIdentifiers.validate(channel) != null;
				} catch (ChannelIdentifierException e) {
					return false;
				}
			})
			.collect(Collectors.toSet());
	}
}
