package net.ornithemc.osl.networking.api;

public class Channels {

	public static final int MAX_LENGTH = 20;

	public static String validate(String channel) {
		if (channel.length() > MAX_LENGTH) {
			throw new RuntimeException("channel \'" + channel + "\' is too long (length " + channel.length() + "/allowed " + MAX_LENGTH + ")");
		}

		return channel;
	}
}
