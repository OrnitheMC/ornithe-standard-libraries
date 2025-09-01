package net.ornithemc.osl.networking.impl;

@SuppressWarnings("serial")
public class ChannelParserException extends RuntimeException {

	private ChannelParserException(String message) {
		super(message);
	}

	private ChannelParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public static ChannelParserException invalid(String channel, Throwable cause) {
		return new ChannelParserException("unable to parse channel from \'" + channel + "\'", cause);
	}

	public static ChannelParserException invalid(String channel, String reason) {
		return new ChannelParserException("unable to parse channel from \'" + channel + "\': " + reason);
	}
}
