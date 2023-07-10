package net.ornithemc.osl.networking.impl;

import net.minecraft.resource.Identifier;

/**
 * Common data channels used internally.
 */
public final class CommonChannels {

	/**
	 * This channel is used upon login by each side to let the other
	 * know which channels it has listeners for.
	 */
	public static final Identifier CHANNELS = new Identifier("osl", "channels");

}
