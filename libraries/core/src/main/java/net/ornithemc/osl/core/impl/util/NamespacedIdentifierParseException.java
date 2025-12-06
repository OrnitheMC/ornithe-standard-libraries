package net.ornithemc.osl.core.impl.util;

@SuppressWarnings("serial")
public class NamespacedIdentifierParseException extends RuntimeException {

	private NamespacedIdentifierParseException(String message) {
		super(message);
	}

	private NamespacedIdentifierParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public static NamespacedIdentifierParseException invalid(String s, Throwable cause) {
		return new NamespacedIdentifierParseException("unable to parse namespaced identifier from \'" + s + "\'", cause);
	}

	public static NamespacedIdentifierParseException invalid(String s, String reason) {
		return new NamespacedIdentifierParseException("unable to parse namespaced identifier from \'" + s + "\': " + reason);
	}
}
