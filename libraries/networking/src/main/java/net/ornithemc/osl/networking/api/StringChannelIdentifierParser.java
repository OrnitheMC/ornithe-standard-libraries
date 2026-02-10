package net.ornithemc.osl.networking.api;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierImpl;
import net.ornithemc.osl.networking.impl.ChannelIdentifierException;
import net.ornithemc.osl.networking.impl.ChannelIdentifierParseException;

/**
 * Utility methods for converting channel identifiers from and to {@link String}s.
 */
public final class StringChannelIdentifierParser {

	/**
	 * The maximum allowed length for the {@code String} representation of a channel identifier.
	 */
	public static final int MAX_LENGTH = ChannelIdentifiers.MAX_LENGTH_NAMESPACE + 1 + ChannelIdentifiers.MAX_LENGTH_IDENTIFIER;

	/**
	 * Convert the given {@code String} to a channel identifier.
	 * The returned identifier may be invalid.
	 * 
	 * @return the channel identifier represented by the {@code String},
	 *   which may or may not be a valid identifier.
	 */
	public static NamespacedIdentifier fromString(String s) {
		int i = s.indexOf('|');

		if (i < 1) {
			// allow null namespaces to support channel ids that do not conform
			// to OSL spec - MC did not enforce a strict spec before 1.13
			return new NamespacedIdentifierImpl("", s);
		} else {
			return new NamespacedIdentifierImpl(s.substring(0, i), s.substring(i + 1));
		}
	}

	/**
	 * Convert the given {@code String} to a channel identifier.
	 * The returned channel identifier is always valid. If no valid channel
	 * identifier can be parsed from the given string, an exception is
	 * thrown.
	 * 
	 * @return the channel identifier represented by the {@code String}.
	 * @throws ChannelIdentifierParseException
	 *   if no valid channel identifier can be parsed from the given {@code String}.
	 */
	public static NamespacedIdentifier fromStringOrThrow(String s) {
		int i = s.indexOf('|');

		try {
			if (i < 0) {
				return ChannelIdentifiers.from(s);
			} else if (i > 0) {
				return ChannelIdentifiers.from(s.substring(0, i), s.substring(i + 1));
			} else {
				throw ChannelIdentifierParseException.invalid(s, "badly formatted");
			}
		} catch (ChannelIdentifierException e) {
			throw ChannelIdentifierParseException.invalid(s, e);
		}
	}

	/**
	 * Convert the given {@code NamespacedIdentifier} to its {@code String} representation.
	 */
	public static String toString(NamespacedIdentifier id) {
		return id.namespace().isEmpty()
			? id.identifier()
			: id.namespace() + "|" + id.identifier();
	}
}
