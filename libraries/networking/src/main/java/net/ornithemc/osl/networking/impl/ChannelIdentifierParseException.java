package net.ornithemc.osl.networking.impl;

@SuppressWarnings("serial")
public class ChannelIdentifierParseException extends RuntimeException {

	private ChannelIdentifierParseException(String message) {
		super(message);
	}

	private ChannelIdentifierParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public static ChannelIdentifierParseException invalid(String id, Throwable cause) {
		return new ChannelIdentifierParseException("unable to parse channel identifier from \'" + id + "\'", cause);
	}

	public static ChannelIdentifierParseException invalid(String id, String reason) {
		return new ChannelIdentifierParseException("unable to parse channel identifier from \'" + id + "\': " + reason);
	}
}
