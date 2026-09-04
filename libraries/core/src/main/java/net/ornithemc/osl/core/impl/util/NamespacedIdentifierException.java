package net.ornithemc.osl.core.impl.util;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

@SuppressWarnings("serial")
public class NamespacedIdentifierException extends RuntimeException {
	private NamespacedIdentifierException(String message) {
		super(message);
	}

	private NamespacedIdentifierException(String message, Throwable cause) {
		super(message, cause);
	}

	public static NamespacedIdentifierException invalid(NamespacedIdentifier id, Throwable cause) {
		return new NamespacedIdentifierException("\'" + id + "\' is not a valid namespaced identifier", cause);
	}

	public static NamespacedIdentifierException invalid(NamespacedIdentifier id, String reason) {
		return new NamespacedIdentifierException("\'" + id + "\' is not a valid namespaced identifier: " + reason);
	}

	public static NamespacedIdentifierException invalidNamespace(String namespace, String reason) {
		return new NamespacedIdentifierException("\'" + namespace + "\' is not a valid namespace: " + reason);
	}

	public static NamespacedIdentifierException invalidPath(String path, String reason) {
		return new NamespacedIdentifierException("\'" + path + "\' is not a valid path: " + reason);
	}
}
