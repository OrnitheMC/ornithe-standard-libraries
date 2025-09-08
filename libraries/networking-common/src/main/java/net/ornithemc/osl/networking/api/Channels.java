package net.ornithemc.osl.networking.api;

import net.ornithemc.osl.networking.impl.ChannelException;

/**
 * Utility methods for creating and validating channels.
 */
public final class Channels {

	/**
	 * The default namespace of channels.
	 * It is recommended to use a custom namespace for your own channels.
	 */
	public static final String DEFAULT_NAMESPACE = "minecraft";

	/**
	 * The maximum length of a channel's namespace string.
	 */
	public static final int MAX_LENGTH_NAMESPACE = Byte.MAX_VALUE;
	/**
	 * The maximum length of a channel's description string.
	 */
	public static final int MAX_LENGTH_DESCRIPTION = Byte.MAX_VALUE;

	/**
	 * Construct a channel with the default namespace and the given description.
	 * 
	 * @deprecated
	 *   Use {@link Channels#make(String)} instead.
	 */
	@Deprecated
	public static Channel from(String description) {
		return from(DEFAULT_NAMESPACE, description);
	}

	/**
	 * Construct a channel from the given namespace and description.
	 * 
	 * @deprecated
	 *   Use {@link Channels#make(String, String)} instead.
	 */
	@Deprecated
	public static Channel from(String namespace, String description) {
		return new Channel(namespace, description);
	}

	/**
	 * Construct and validate a channel with the default namespace and the given description.
	 */
	public static Channel make(String description) {
		return make(DEFAULT_NAMESPACE, description);
	}

	/**
	 * Construct and validate a channel from the given namespace and description.
	 */
	public static Channel make(String namespace, String description) {
		return validate(from(namespace, description));
	}

	/**
	 * Check whether the given channel is valid, or throw an exception.
	 */
	public static Channel validate(Channel channel) {
		try {
			validateNamespace(channel.getNamespace());
			validateDescription(channel.getDescription());

			return channel;
		} catch (ChannelException e) {
			throw ChannelException.invalid(channel, e);
		}
	}

	/**
	 * Check that the given namespace is valid for a channel.
	 */
	public static String validateNamespace(String namespace) {
		if (namespace == null || namespace.isEmpty()) {
			throw ChannelException.invalidNamespace(namespace, "null or empty");
		}
		if (namespace.length() > MAX_LENGTH_NAMESPACE) {
			throw ChannelException.invalidNamespace(namespace, "length " + namespace.length() + " is greater than maximum allowed " + MAX_LENGTH_NAMESPACE);
		}
		if (!namespace.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9'))) {
			throw ChannelException.invalidNamespace(namespace, "contains illegal characters - only [a-z0-9-._] are allowed");
		}

		return namespace;
	}

	/**
	 * Check that the given description is valid for a channel.
	 */
	public static String validateDescription(String description) {
		if (description == null || description.isEmpty()) {
			throw ChannelException.invalidDescription(description, "null or empty");
		}
		if (description.length() > MAX_LENGTH_DESCRIPTION) {
			throw ChannelException.invalidDescription(description, "length " + description.length() + " is greater than maximum allowed " + MAX_LENGTH_DESCRIPTION);
		}
		if (!description.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9'))) {
			throw ChannelException.invalidDescription(description, "contains illegal characters - only [a-z0-9-._/] are allowed");
		}

		return description;
	}
}
