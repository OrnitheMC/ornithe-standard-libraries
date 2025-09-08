package net.ornithemc.osl.networking.impl;

import net.ornithemc.osl.networking.api.Channel;

@SuppressWarnings("serial")
public class ChannelException extends RuntimeException {

	private ChannelException(String message) {
		super(message);
	}

	private ChannelException(String message, Throwable cause) {
		super(message, cause);
	}

	public static ChannelException invalid(Channel channel, Throwable cause) {
		return new ChannelException("\'" + channel + "\' is not a valid channel", cause);
	}

	public static ChannelException invalid(Channel channel, String reason) {
		return new ChannelException("\'" + channel + "\' is not a valid channel: " + reason);
	}

	public static ChannelException invalidNamespace(String namespace, String reason) {
		return new ChannelException("\'" + namespace + "\' is not a valid channel namespace: " + reason);
	}

	public static ChannelException invalidDescription(String description, String reason) {
		return new ChannelException("\'" + description + "\' is not a valid channel description: " + reason);
	}
}
