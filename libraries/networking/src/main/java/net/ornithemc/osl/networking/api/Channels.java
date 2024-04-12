package net.ornithemc.osl.networking.api;

public class Channels {

	/**
	 * The maximum String length for a custom payload channel.
	 */
	public static final int MAX_LENGTH = 20;

	/**
	 * Check that the given String channel is valid, or throw an exception.
	 */
	public static String validate(String channel) {
		if (channel.length() > MAX_LENGTH) {
			throw new RuntimeException("channel \'" + channel + "\' is too long (length " + channel.length() + "/allowed " + MAX_LENGTH + ")");
		}

		return channel;
	}
}
