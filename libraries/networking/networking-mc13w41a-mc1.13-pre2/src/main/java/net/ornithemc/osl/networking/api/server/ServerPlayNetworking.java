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

	public static void registerListener(String channel, Listener listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener);
	}

	public static void registerListenerAsync(String channel, Listener listener) {
		ServerPlayNetworkingImpl.registerListenerAsync(channel, listener);
	}

	public static void unregisterListener(String channel) {
		ServerPlayNetworkingImpl.unregisterListener(channel);
	}

	public static boolean canSend(ServerPlayerEntity player, String channel) {
		return ServerPlayNetworkingImpl.canSend(player, channel);
	}

	public static void send(ServerPlayerEntity player, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(player, channel, writer);
	}

	public static void send(ServerPlayerEntity player, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(player, channel, data);
	}

	public static void send(Collection<ServerPlayerEntity> players, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(players, channel, writer);
	}

	public static void send(Collection<ServerPlayerEntity> players, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(players, channel, data);
	}

	public static void send(ServerWorld world, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(world, channel, writer);
	}

	public static void send(ServerWorld world, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(world, channel, data);
	}

	public static void send(MinecraftServer server, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.send(server, channel, writer);
	}

	public static void send(MinecraftServer server, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.send(server, channel, data);
	}

	/**
	 * Send a packet to the client without checking if the given channel is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(ServerPlayerEntity player, String channel, Consumer<PacketByteBuf> writer) {
		ServerPlayNetworkingImpl.doSend(player, channel, writer);
	}

	/**
	 * Send a packet to the client without checking if the given channel is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the client.
	 */
	public static void doSend(ServerPlayerEntity player, String channel, PacketByteBuf data) {
		ServerPlayNetworkingImpl.doSend(player, channel, data);
	}

	public interface Listener {

		boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, PacketByteBuf data);

	}
}
