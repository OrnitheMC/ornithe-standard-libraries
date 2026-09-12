package net.ornithemc.osl.networking.api;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierImpl;
import net.ornithemc.osl.networking.impl.ChannelIdentifierParseException;
import net.ornithemc.osl.networking.impl.LegacyChannelIdentifierImpl;

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
		int i = s.indexOf(LegacyChannelIdentifier.SEPARATOR);

		if (i > 0) {
			return new LegacyChannelIdentifierImpl(s.substring(0, i), s.substring(i + 1));
		}

		i = s.indexOf(NamespacedIdentifier.SEPARATOR);

		if (i < 0) {
			// allow empty namespaces since MC did not enforce a strict spec before 1.13
			return new LegacyChannelIdentifierImpl("", s);
		}

		return new NamespacedIdentifierImpl(s.substring(0, i), s.substring(i + 1));
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
		int i = s.indexOf(LegacyChannelIdentifier.SEPARATOR);

		if (i > 0) {
			return new LegacyChannelIdentifierImpl(s.substring(0, i), s.substring(i + 1));
		}

		i = s.indexOf(NamespacedIdentifier.SEPARATOR);

		if (i < 0) {
			// allow empty namespaces since MC did not enforce a strict spec before 1.13
			return new LegacyChannelIdentifierImpl("", s);
		}

		return ChannelIdentifiers.from(s.substring(0, i), s.substring(i + 1));
	}

	/**
	 * Convert the given {@code NamespacedIdentifier} to its {@code String} representation.
	 */
	public static String toString(NamespacedIdentifier id) {
		return id.toString();
	}
}
