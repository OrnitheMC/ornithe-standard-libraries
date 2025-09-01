package net.ornithemc.osl.networking.api;

import java.util.Objects;

/**
 * This class represents a channel over which packets are sent.
 * Each channel has a listener that only receives packets that
 * are sent over that channel.
 */
public final class Channel {

	/**
	 * The namespace to which this channel belongs.
	 */
	private String namespace;
	/**
	 * The description of this channel.
	 */
	private String description;

	Channel(String namespace, String description) {
		this.namespace = namespace;
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Channel)) {
			return false;
		}
		Channel c = (Channel) o;
		return namespace.equals(c.namespace) && description.equals(c.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, description);
	}

	@Override
	public String toString() {
		return "channel[namespace: " + namespace + ", description: " + description + "]";
	}

	/**
	 * @return the namespace to which this channel belongs
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @return the description of this channel
	 */
	public String getDescription() {
		return description;
	}
}
