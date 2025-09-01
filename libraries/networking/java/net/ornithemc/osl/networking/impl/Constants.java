package net.ornithemc.osl.networking.impl;

public class Constants {

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

}
