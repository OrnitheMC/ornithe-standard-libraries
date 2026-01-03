package net.ornithemc.osl.networking.impl;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.ChannelIdentifiers;

public final class Constants {

	/**
	 * The packet id for custom payloads, matches that
	 * of Vanilla custom payloads in 11w49a and above.
	 */
	public static final int CUSTOM_PAYLOAD_PACKET_ID = 250;
	/**
	 * The string used to modify the Vanilla handshake.
	 * It needs to be an invalid Minecraft username.
	 */
	public static final String OSL_HANDSHAKE_KEY = "\0OrnitheMC";
	/**
	 * A number that describes the OSL handshake protocol, in case
	 * the data that is sent is expanded, reduced, or reorganized.
	 */
	public static final byte OSL_HANDSHAKE_PROTOCOL = 1;
	/**
	 * The channel identifier for OSL's handshake payload.
	 */
	public static final NamespacedIdentifier OSL_HANDSHAKE_CHANNEL = ChannelIdentifiers.from("osl", "handshake");

}
