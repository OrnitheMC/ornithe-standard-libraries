package net.ornithemc.osl.networking.impl;

import java.util.HashMap;
import java.util.Map;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public final class ChannelRegistryImpl {

	private static final Map<NamespacedIdentifier, ChannelSettings> SETTINGS = new HashMap<>();

	public static NamespacedIdentifier register(NamespacedIdentifier channel) {
		return register(channel, new ChannelSettings());
	}

	public static NamespacedIdentifier register(NamespacedIdentifier channel, boolean clientbound, boolean serverbound) {
		return register(channel, new ChannelSettings(clientbound, serverbound));
	}

	public static NamespacedIdentifier register(NamespacedIdentifier channel, ChannelSettings settings) {
		SETTINGS.compute(channel, (key, value) -> {
			if (value != null && !value.is(settings)) {
				throw new IllegalArgumentException("channel \'" + channel + "\' was already registered with different settings!");
			}

			return settings;
		});

		return channel;
	}

	public static boolean contains(NamespacedIdentifier channel) {
		return SETTINGS.containsKey(channel);
	}

	public static ChannelSettings getSettings(NamespacedIdentifier channel) {
		return SETTINGS.get(channel);
	}
}
