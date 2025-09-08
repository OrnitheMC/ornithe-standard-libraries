package net.ornithemc.osl.networking.api;

import net.ornithemc.osl.networking.impl.ChannelException;
import net.ornithemc.osl.networking.impl.ChannelParserException;

/**
 * Utility methods for converting {@link Channel}s from and to {@link String}s.
 */
public final class StringChannelParser {

	/**
	 * The maximum allowed length for the String representation of a Channel.
	 */
	public static final int MAX_LENGTH = Channels.MAX_LENGTH_NAMESPACE + 1 + Channels.MAX_LENGTH_DESCRIPTION;

	/**
	 * Convert the given String to a Channel.
	 * The returned Channel may be invalid.
	 * 
	 * @return the Channel represented by the String,
	 *   or {@code null} if the String was badly formatted.
	 */
	public static Channel fromString(String s) {
		int i = s.indexOf('|');

		if (i < 0) {
			return Channels.from(s);
		} else if (i > 0) {
			return Channels.from(s.substring(0, i), s.substring(i + 1));
		} else {
			return null;
		}
	}

	/**
	 * Convert the given String to a Channel.
	 * The returned Channel is always valid. If no valid Channel can be
	 * parsed from the given String, an exception is thrown.
	 * 
	 * @return the Channel represented by the String.
	 * @throws ChannelParserException
	 *   if no valid Channel can be parsed from the given String.
	 */
	public static Channel fromStringOrThrow(String s) {
		int i = s.indexOf('|');

		try {
			if (i < 0) {
				return Channels.make(s);
			} else if (i > 0) {
				return Channels.make(s.substring(0, i), s.substring(i + 1));
			} else {
				throw ChannelParserException.invalid(s, "badly formatted");
			}
		} catch (ChannelException e) {
			throw ChannelParserException.invalid(s, e);
		}
	}

	/**
	 * Convert the given Channel to its String representation.
	 */
	public static String toString(Channel channel) {
		return channel.getNamespace() + "|" + channel.getDescription();
	}
}

