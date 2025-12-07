package net.ornithemc.osl.networking.api.client;

import java.util.function.Supplier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;

public final class ClientPlayNetworking {

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * The data will be deserialized into a {@code CustomPayload} object of the given type.
	 */
	public static <T extends PacketPayload> void registerListener(NamespacedIdentifier channel, Supplier<T> initializer, ClientPacketListener.Payload<T> listener) {
		ClientPlayNetworkingImpl.registerListener(channel, initializer, listener);
	}

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * The data will be wrapped in a {@link PacketBuffer} from which it can be read.
	 */
	public static void registerListener(NamespacedIdentifier channel, ClientPacketListener.Buffer listener) {
		ClientPlayNetworkingImpl.registerListener(channel, listener);
	}

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * The data will be given as a raw {@code byte[]}.
	 */
	public static void registerLegacyListener(NamespacedIdentifier channel, ClientPacketListener.Bytes listener) {
		ClientPlayNetworkingImpl.registerListener(channel, listener);
	}

	/**
	 * Remove the listener registered to the given channel.
	 */
	public static void unregisterListener(NamespacedIdentifier channel) {
		ClientPlayNetworkingImpl.unregisterListener(channel);
	}

	/**
	 * Check whether the connection is ready for data to be sent to the server.
	 */
	public static boolean isPlayReady() {
		return ClientPlayNetworkingImpl.isPlayReady();
	}

	/**
	 * Check whether the given channel is open for data to be sent through it.
	 * This method will return {@code false} if the client is not connected to a
	 * server, or if the server has no listeners for the given channel.
	 */
	public static boolean isPlayReady(NamespacedIdentifier channel) {
		return ClientPlayNetworkingImpl.isPlayReady(channel);
	}

	/**
	 * Send a packet to the server through the given channel. The payload will
	 * only be written if the channel is open.
	 */
	public static void send(NamespacedIdentifier channel, PacketPayload payload) {
		ClientPlayNetworkingImpl.send(channel, payload);
	}

	/**
	 * Send a packet to the server through the given channel. The writer will
	 * only be called if the channel is open.
	 */
	public static void send(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ClientPlayNetworkingImpl.send(channel, writer);
	}

	/**
	 * Send a packet to the server through the given channel.
	 */
	public static void send(NamespacedIdentifier channel, PacketBuffer buffer) {
		ClientPlayNetworkingImpl.send(channel, buffer);
	}

	/**
	 * Send a packet to the server through the given channel.
	 */
	public static void send(NamespacedIdentifier channel, byte[] bytes) {
		ClientPlayNetworkingImpl.send(channel, bytes);
	}

	/**
	 * Send a packet to the server through the given channel, without checking
	 * whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the server.
	 */
	public static void sendNoCheck(NamespacedIdentifier channel, PacketPayload payload) {
		ClientPlayNetworkingImpl.sendNoCheck(channel, payload);
	}

	/**
	 * Send a packet to the server through the given channel, without checking
	 * whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the server.
	 */
	public static void sendNoCheck(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ClientPlayNetworkingImpl.sendNoCheck(channel, writer);
	}

	/**
	 * Send a packet to the server through the given channel, without checking
	 * whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the server.
	 */
	public static void sendNoCheck(NamespacedIdentifier channel, PacketBuffer buffer) {
		ClientPlayNetworkingImpl.sendNoCheck(channel, buffer);
	}

	/**
	 * Send a packet to the server through the given channel, without checking
	 * whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the server.
	 */
	public static void sendNoCheck(NamespacedIdentifier channel, byte[] bytes) {
		ClientPlayNetworkingImpl.sendNoCheck(channel, bytes);
	}
}
