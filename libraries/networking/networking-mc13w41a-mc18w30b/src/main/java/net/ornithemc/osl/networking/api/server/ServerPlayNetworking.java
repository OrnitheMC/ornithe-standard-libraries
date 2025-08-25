package net.ornithemc.osl.networking.api.server;

import java.util.function.Supplier;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public final class ServerPlayNetworking {

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener will only be called from the main thread.
	 */
	public static <T extends PacketPayload> void registerListener(NamespacedIdentifier channel, Supplier<T> initializer, ServerPacketListener.Payload<T> listener) {
		ServerPlayNetworkingImpl.registerListener(channel, initializer, listener);
	}

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener may be called off the main thread.
	 */
	public static <T extends PacketPayload> void registerListenerAsync(NamespacedIdentifier channel, Supplier<T> initializer, ServerPacketListener.Payload<T> listener) {
		ServerPlayNetworkingImpl.registerListenerAsync(channel, initializer, listener);
	}

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener will only be called from the main thread.
	 */
	public static void registerListener(NamespacedIdentifier channel, ServerPacketListener.Buffer listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener);
	}

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener may be called off the main thread.
	 */
	public static void registerListenerAsync(NamespacedIdentifier channel, ServerPacketListener.Buffer listener) {
		ServerPlayNetworkingImpl.registerListenerAsync(channel, listener);
	}

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener will only be called from the main thread.
	 */
	public static void registerListener(NamespacedIdentifier channel, ServerPacketListener.Bytes listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener);
	}

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener may be called off the main thread.
	 */
	public static void registerListenerAsync(NamespacedIdentifier channel, ServerPacketListener.Bytes listener) {
		ServerPlayNetworkingImpl.registerListenerAsync(channel, listener);
	}

	/**
	 * Remove the listener registered to the given channel.
	 */
	public static void unregisterListener(NamespacedIdentifier channel) {
		ServerPlayNetworkingImpl.unregisterListener(channel);
	}

	/**
	 * Check whether the connection is ready for data to be sent to the client.
	 */
	public static boolean isPlayReady(ServerPlayerEntity player) {
		return ServerPlayNetworkingImpl.isPlayReady(player);
	}

	/**
	 * Check whether the given channel is ready for data to be sent through it.
	 * This method will return {@code false} if the client has no listeners for
	 * the given channel.
	 */
	public static boolean isPlayReady(ServerPlayerEntity player, NamespacedIdentifier channel) {
		return ServerPlayNetworkingImpl.isPlayReady(player, channel);
	}

	/**
	 * Send a packet to the given player through the given channel. The payload
	 * will only be written if the channel is open.
	 */
	public static void send(ServerPlayerEntity player, NamespacedIdentifier channel, PacketPayload payload) {
		ServerPlayNetworkingImpl.send(player, channel, payload);
	}

	/**
	 * Send a packet to the given player through the given channel. The writer
	 * will only be called if the channel is open.
	 */
	public static void send(ServerPlayerEntity player, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ServerPlayNetworkingImpl.send(player, channel, writer);
	}

	/**
	 * Send a packet to the given player through the given channel.
	 */
	public static void send(ServerPlayerEntity player, NamespacedIdentifier channel, PacketBuffer buffer) {
		ServerPlayNetworkingImpl.send(player, channel, buffer);
	}

	/**
	 * Send a packet to the given player through the given channel.
	 */
	public static void send(ServerPlayerEntity player, NamespacedIdentifier channel, byte[] bytes) {
		ServerPlayNetworkingImpl.send(player, channel, bytes);
	}

	/**
	 * Send a packet to the given players through the given channel. The payload
	 * will only be written if the channel is open for at least one player.
	 */
	public static void send(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketPayload payload) {
		ServerPlayNetworkingImpl.send(players, channel, payload);
	}

	/**
	 * Send a packet to the given players through the given channel. The writer
	 * will only be called if the channel is open for at least one player.
	 */
	public static void send(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ServerPlayNetworkingImpl.send(players, channel, writer);
	}

	/**
	 * Send a packet to the given players through the given channel.
	 */
	public static void send(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketBuffer buffer) {
		ServerPlayNetworkingImpl.send(players, channel, buffer);
	}

	/**
	 * Send a packet to the given players through the given channel.
	 */
	public static void send(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, byte[] bytes) {
		ServerPlayNetworkingImpl.send(players, channel, bytes);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel. The payload will only be written if the channel is open for at
	 * least one player.
	 */
	public static void send(int dimension, NamespacedIdentifier channel, PacketPayload payload) {
		ServerPlayNetworkingImpl.send(dimension, channel, payload);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel. The writer will only be called if the channel is open for at
	 * least one player.
	 */
	public static void send(int dimension, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ServerPlayNetworkingImpl.send(dimension, channel, writer);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel.
	 */
	public static void send(int dimension, NamespacedIdentifier channel, PacketBuffer buffer) {
		ServerPlayNetworkingImpl.send(dimension, channel, buffer);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel.
	 */
	public static void send(int dimension, NamespacedIdentifier channel, byte[] bytes) {
		ServerPlayNetworkingImpl.send(dimension, channel, bytes);
	}

	/**
	 * Send a packet to all players through the given channel. The payload will
	 * only be written if the channel is open for at least one player.
	 */
	public static void send(NamespacedIdentifier channel, PacketPayload payload) {
		ServerPlayNetworkingImpl.send(channel, payload);
	}

	/**
	 * Send a packet to all players through the given channel. The writer will
	 * only be called if the channel is open for at least one player.
	 */
	public static void send(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ServerPlayNetworkingImpl.send(channel, writer);
	}

	/**
	 * Send a packet to all players through the given channel.
	 */
	public static void send(NamespacedIdentifier channel, PacketBuffer buffer) {
		ServerPlayNetworkingImpl.send(channel, buffer);
	}

	/**
	 * Send a packet to all players through the given channel.
	 */
	public static void send(NamespacedIdentifier channel, byte[] bytes) {
		ServerPlayNetworkingImpl.send(channel, bytes);
	}

	/**
	 * Send a packet to the given player through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(ServerPlayerEntity player, NamespacedIdentifier channel, PacketPayload payload) {
		ServerPlayNetworkingImpl.sendNoCheck(player, channel, payload);
	}

	/**
	 * Send a packet to the given player through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(ServerPlayerEntity player, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ServerPlayNetworkingImpl.sendNoCheck(player, channel, writer);
	}

	/**
	 * Send a packet to the given player through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(ServerPlayerEntity player, NamespacedIdentifier channel, PacketBuffer buffer) {
		ServerPlayNetworkingImpl.sendNoCheck(player, channel, buffer);
	}

	/**
	 * Send a packet to the given player through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(ServerPlayerEntity player, NamespacedIdentifier channel, byte[] bytes) {
		ServerPlayNetworkingImpl.sendNoCheck(player, channel, bytes);
	}

	/**
	 * Send a packet to the given players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketPayload payload) {
		ServerPlayNetworkingImpl.sendNoCheck(players, channel, payload);
	}

	/**
	 * Send a packet to the given players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ServerPlayNetworkingImpl.sendNoCheck(players, channel, writer);
	}

	/**
	 * Send a packet to the given players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketBuffer buffer) {
		ServerPlayNetworkingImpl.sendNoCheck(players, channel, buffer);
	}

	/**
	 * Send a packet to the given players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, byte[] bytes) {
		ServerPlayNetworkingImpl.sendNoCheck(players, channel, bytes);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel, without checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(int dimension, NamespacedIdentifier channel, PacketPayload payload) {
		ServerPlayNetworkingImpl.sendNoCheck(dimension, channel, payload);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel, without checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(int dimension, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ServerPlayNetworkingImpl.sendNoCheck(dimension, channel, writer);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel, without checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(int dimension, NamespacedIdentifier channel, PacketBuffer buffer) {
		ServerPlayNetworkingImpl.sendNoCheck(dimension, channel, buffer);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel, without checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(int dimension, NamespacedIdentifier channel, byte[] bytes) {
		ServerPlayNetworkingImpl.sendNoCheck(dimension, channel, bytes);
	}

	/**
	 * Send a packet to all players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(NamespacedIdentifier channel, PacketPayload payload) {
		ServerPlayNetworkingImpl.sendNoCheck(channel, payload);
	}

	/**
	 * Send a packet to all players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		ServerPlayNetworkingImpl.sendNoCheck(channel, writer);
	}

	/**
	 * Send a packet to all players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(NamespacedIdentifier channel, PacketBuffer buffer) {
		ServerPlayNetworkingImpl.sendNoCheck(channel, buffer);
	}

	/**
	 * Send a packet to all players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void sendNoCheck(NamespacedIdentifier channel, byte[] bytes) {
		ServerPlayNetworkingImpl.sendNoCheck(channel, bytes);
	}
}
