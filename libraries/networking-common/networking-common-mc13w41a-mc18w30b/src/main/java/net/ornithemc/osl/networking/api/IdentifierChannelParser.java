package net.ornithemc.osl.networking.api;

import net.minecraft.resource.Identifier;

import net.ornithemc.osl.networking.impl.ChannelException;
import net.ornithemc.osl.networking.impl.ChannelParserException;

/**
 * Utility methods for converting {@link Channel}s from and to {@link Identifier}s.
 */
public final class IdentifierChannelParser {

	/**
	 * Convert the given String to a Channel.
	 * The returned Channel may be invalid.
	 * 
	 * @return the Channel represented by the Identifier.
	 */
	public static Channel fromIdentifier(Identifier id) {
		return Channels.from(id.getNamespace(), id.getPath());
	}

	/**
	 * Convert the given Identifier to a Channel.
	 * The returned Channel is always valid. If no valid Channel can be
	 * parsed from the given Identifier, an exception is thrown.
	 * 
	 * @return the Channel represented by the Identifier.
	 * @throws ChannelParserException
	 *   if no valid Channel can be parsed from the given Identifier.
	 */
	public static Channel fromStringOrThrow(Identifier id) {
		try {
			return Channels.make(id.getNamespace(), id.getPath());
		} catch (ChannelException e) {
			throw ChannelParserException.invalid(id.toString(), e);
		}
	}

	/**
	 * Convert the given Channel to its String representation.
	 */
	public static Identifier toIdentifier(Channel channel) {
		return new Identifier(channel.getNamespace(), channel.getDescription());
	}
}

