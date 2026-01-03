package net.ornithemc.osl.networking.api;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.impl.ChannelRegistryImpl;

public final class ChannelRegistry {

	/**
	 * Register a channel with default settings. This means the channel will accept
	 * both client-bound and server-bound packets. For finer control over channel
	 * settings, see {@linkplain #register(NamespacedIdentifier, boolean, boolean)}.
	 * 
	 * @return the registered channel.
	 */
	public static NamespacedIdentifier register(NamespacedIdentifier channel) {
		return ChannelRegistryImpl.register(channel);
	}

	/**
	 * Register a channel with the given settings.
	 * <p>
	 * A channel should be marked as client-bound if the client is expected to
	 * receive and handle packets sent over that channel.
	 * <br>
	 * A channel should be marked as server-bound if the server is expected to
	 * receive and handle packets sent over that channel.
	 * <p>
	 * A channel that has not been registered can not have any listeners and is
	 * considered 'closed'.
	 * 
	 * @return the registered channel.
	 */
	public static NamespacedIdentifier register(NamespacedIdentifier channel, boolean clientbound, boolean serverbound) {
		return ChannelRegistryImpl.register(channel, clientbound, serverbound);
	}
}
