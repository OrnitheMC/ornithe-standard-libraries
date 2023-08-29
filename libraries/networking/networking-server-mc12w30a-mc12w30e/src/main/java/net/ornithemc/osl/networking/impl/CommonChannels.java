package net.ornithemc.osl.networking.impl;

/**
 * Common data channels used internally.
 * 
 * The channel identifiers follow conventions set by Vanilla custom
 * payload packets. They consist of a domain name and a channel name,
 * separated by a {@code |} character.
 */
public final class CommonChannels {

	/**
	 * This channel is used upon login by each side to let the other
	 * know which channels it has listeners for.
	 */
	public static final String CHANNELS = "OSL|Channels";

}
