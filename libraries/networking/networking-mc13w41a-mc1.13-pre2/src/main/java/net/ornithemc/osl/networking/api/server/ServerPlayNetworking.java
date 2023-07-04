package net.ornithemc.osl.networking.api.server;

import java.util.Collection;
import java.util.function.Consumer;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;

import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public final class ServerPlayNetworking {

	/**
	 * Registers a listener to receive data from the client through the given channel.
	 * This listener will only be called from the main thread.
	 */
	public static void registerListener(String channel, Listener listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener);
	}

	/**
	 * Registers a listener to receive data from the client through the given channel.
	 * This listener may be called off the main thread.
	 */
	public static void registerListenerAsync(String channel, Listener listener) {
		ServerPlayNetworkingImpl.registerListenerAsync(channel, listener);
	}

	/**
	 * Removes the listener registered to the given channel.
	 */
	public static void unregisterListener(String channel) {
		ServerPlayNetworkingImpl.unregisterListener(channel);
	}

	/**
	 * Returns whether the given channel is open for data to be sent through it.
	 * This method will return {@code false} if the client has no listeners for
	 * the given channel.
	 */
	public static boolean canSend(ServerPlayerEntity player, String channel) {
		return ServerPlayNetworkingImpl.canSend(player, channel);
	}

	/**
	 * Sends a packet to the client through the given channel. The writer will
	 * only be called if the channel is open.
	 */
	public static void send(ServerPlayerEntity player, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(player, channel, writer);
	}

	/**
	 * Sends a packet to the client through the given channel.
	 */
	public static void send(ServerPlayerEntity player, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(player, channel, data);
	}

	/**
	 * Sends a packet to the given clients through the given channel. The writer
	 * will only be called if the channel is open.
	 */
	public static void send(Collection<ServerPlayerEntity> players, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(players, channel, writer);
	}

	/**
	 * Sends a packet to the given clients through the given channel.
	 */
	public static void send(Collection<ServerPlayerEntity> players, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(players, channel, data);
	}

	/**
	 * Sends a packet to all clients in the given world through the given channel.
	 * The writer will only be called if the channel is open.
	 */
	public static void send(ServerWorld world, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(world, channel, writer);
	}

	/**
	 * Sends a packet to all clients in the given world through the given channel.
	 */
	public static void send(ServerWorld world, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(world, channel, data);
	}

	/**
	 * Sends a packet to all clients through the given channel. The writer
	 * will only be called if the channel is open.
	 */
	public static void send(MinecraftServer server, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(server, channel, writer);
	}

	/**
	 * Sends a packet to all clients through the given channel.
	 */
	public static void send(MinecraftServer server, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(server, channel, data);
	}

	/**
	 * Send a packet to the client through the given channel, without checking
	 * whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(ServerPlayerEntity player, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.doSend(player, channel, writer);
	}

	/**
	 * Send a packet to the client through the given channel, without checking
	 * whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(ServerPlayerEntity player, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.doSend(player, channel, data);
	}

	public interface Listener {

		/**
		 * Receive incoming data. Returns whether the data is consumed.
		 * Should only return {@code false} if the data is completely ignored.
		 */
		boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, PacketByteBuf data);

	}
}
