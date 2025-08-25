package net.ornithemc.osl.networking.api;

import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierImpl;
import net.ornithemc.osl.networking.impl.ChannelIdentifierException;
import net.ornithemc.osl.networking.impl.ChannelIdentifierParseException;

/**
 * Utility methods for converting {@link NamespacedIdentifier}s from and to {@link Identifier}s.
 */
public final class IdentifierChannelIdentifierParser {

	/**
	 * Convert the given {@code Identifier} to a {@link NamespacedIdentifier}.
	 * The returned channel identifier may be invalid.
	 * 
	 * @return the {@code NamespacedIdentifier} represented by the {@code Identifier}.
	 */
	public static NamespacedIdentifier fromIdentifier(Identifier id) {
		return new NamespacedIdentifierImpl(id.getNamespace(), id.getPath());
	}

	/**
	 * Convert the given {@code Identifier} to a {@code NamespacedIdentifier}.
	 * The returned channel identifier is always valid. If no valid channel
	 * identifier can be parsed from the given identifier, an exception is
	 * thrown.
	 * 
	 * @return the {@code NamespacedIdentifier} represented by the {@code Identifier}.
	 * @throws ChannelIdentifierParseException
	 *   if no valid {@code NamespacedIdentifier} can be parsed from the given {@code Identifier}.
	 */
	public static NamespacedIdentifier fromIdentifierOrThrow(Identifier id) {
		try {
			return ChannelIdentifiers.from(id.getNamespace(), id.getPath());
		} catch (ChannelIdentifierException e) {
			throw ChannelIdentifierParseException.invalid(id.toString(), e);
		}
	}

	/**
	 * Convert the given {@code NamespacedIdentifier} to its {@code Identifier} representation.
	 */
	public static Identifier toIdentifier(NamespacedIdentifier id) {
		return new Identifier(id.namespace(), id.identifier());
	}
}
