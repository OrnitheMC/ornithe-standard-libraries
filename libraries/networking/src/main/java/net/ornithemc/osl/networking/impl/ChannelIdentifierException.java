package net.ornithemc.osl.networking.impl;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

@SuppressWarnings("serial")
public class ChannelIdentifierException extends RuntimeException {

	private ChannelIdentifierException(String message) {
		super(message);
	}

	private ChannelIdentifierException(String message, Throwable cause) {
		super(message, cause);
	}

	public static ChannelIdentifierException invalid(NamespacedIdentifier id, Throwable cause) {
		return new ChannelIdentifierException("\'" + id + "\' is not a valid channel identifier", cause);
	}

	public static ChannelIdentifierException invalid(NamespacedIdentifier id, String reason) {
		return new ChannelIdentifierException("\'" + id + "\' is not a valid channel identifier: " + reason);
	}

	public static ChannelIdentifierException invalidNamespace(String namespace, String reason) {
		return new ChannelIdentifierException("\'" + namespace + "\' is not a valid namespace: " + reason);
	}

	public static ChannelIdentifierException invalidIdentifier(String identifier, String reason) {
		return new ChannelIdentifierException("\'" + identifier + "\' is not a valid identifier: " + reason);
	}
}
