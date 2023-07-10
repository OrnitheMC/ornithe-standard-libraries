package net.ornithemc.osl.networking.api.server;

import java.util.function.Consumer;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public final class ServerPlayNetworking {

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener will only be called from the main thread.
	 */
	public static void registerListener(Identifier channel, Listener listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener);
	}

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener may be called off the main thread.
	 */
	public static void registerListenerAsync(Identifier channel, Listener listener) {
		ServerPlayNetworkingImpl.registerListenerAsync(channel, listener);
	}

	/**
	 * Remove the listener registered to the given channel.
	 */
	public static void unregisterListener(Identifier channel) {
		ServerPlayNetworkingImpl.unregisterListener(channel);
	}

	/**
	 * Check whether the given channel is open for data to be sent through it.
	 * This method will return {@code false} if the client has no listeners for
	 * the given channel.
	 */
	public static boolean canSend(ServerPlayerEntity player, Identifier channel) {
		return ServerPlayNetworkingImpl.canSend(player, channel);
	}

	/**
	 * Send a packet to the given player through the given channel. The writer
	 * will only be called if the channel is open.
	 */
	public static void send(ServerPlayerEntity player, Identifier channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(player, channel, writer);
	}

	/**
	 * Send a packet to the given player through the given channel.
	 */
	public static void send(ServerPlayerEntity player, Identifier channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(player, channel, data);
	}

	/**
	 * Send a packet to the given players through the given channel. The writer
	 * will only be called if the channel is open for at least one player.
	 */
	public static void send(Iterable<ServerPlayerEntity> players, Identifier channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(players, channel, writer);
	}

	/**
	 * Send a packet to the given players through the given channel.
	 */
	public static void send(Iterable<ServerPlayerEntity> players, Identifier channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(players, channel, data);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel. The writer will only be called if the channel is open for at
	 * least one player.
	 */
	public static void send(int dimension, Identifier channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(dimension, channel, writer);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel.
	 */
	public static void send(int dimension, Identifier channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(dimension, channel, data);
	}

	/**
	 * Send a packet to all players through the given channel. The writer will
	 * only be called if the channel is open for at least one player.
	 */
	public static void send(Identifier channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(channel, writer);
	}

	/**
	 * Send a packet to all players through the given channel.
	 */
	public static void send(Identifier channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(channel, data);
	}

	/**
	 * Send a packet to the given player through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(ServerPlayerEntity player, Identifier channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.doSend(player, channel, writer);
	}

	/**
	 * Send a packet to the given player through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(ServerPlayerEntity player, Identifier channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.doSend(player, channel, data);
	}

	/**
	 * Send a packet to the given players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(Iterable<ServerPlayerEntity> players, Identifier channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.doSend(players, channel, writer);
	}

	/**
	 * Send a packet to the given players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(Iterable<ServerPlayerEntity> players, Identifier channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.doSend(players, channel, data);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel, without checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(int dimension, Identifier channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.doSend(dimension, channel, writer);
	}

	/**
	 * Send a packet to the players in the given dimension through the given
	 * channel, without checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(int dimension, Identifier channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.doSend(dimension, channel, data);
	}

	/**
	 * Send a packet to all players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(Identifier channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.doSend(channel, writer);
	}

	/**
	 * Send a packet to all players through the given channel, without
	 * checking whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(Identifier channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.doSend(channel, data);
	}

	public interface Listener {

		/**
		 * Receive incoming data from the client.
		 *  
		 * @return 
		 *  Whether the data is consumed. Should only return {@code false} if the
		 *  data is completely ignored.
		 */
		boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, PacketByteBuf data);

	}
}
