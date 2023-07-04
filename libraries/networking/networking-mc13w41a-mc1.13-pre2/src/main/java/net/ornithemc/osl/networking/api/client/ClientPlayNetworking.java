package net.ornithemc.osl.networking.api.client;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;

public final class ClientPlayNetworking {

	public static void registerListener(String channel, Listener listener) {
		ClientPlayNetworkingImpl.registerListener(channel, listener);
	}

	public static void registerListenerAsync(String channel, Listener listener) {
		ClientPlayNetworkingImpl.registerListenerAsync(channel, listener);
	}

	public static void unregisterListener(String channel) {
		ClientPlayNetworkingImpl.unregisterListener(channel);
	}

	public static boolean canSend(String channel) {
		return ClientPlayNetworkingImpl.canSend(channel);
	}

	public static void send(String channel, Consumer<PacketByteBuf> writer) {
		ClientPlayNetworkingImpl.send(channel, writer);
	}

	public static void send(String channel, PacketByteBuf data) {
		ClientPlayNetworkingImpl.send(channel, data);
	}

	/**
	 * Send a packet to the server without checking if the given channel is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the server.
	 */
	public static void doSend(String channel, Consumer<PacketByteBuf> writer) {
		ClientPlayNetworkingImpl.doSend(channel, writer);
	}

	/**
	 * Send a packet to the server without checking if the given channel is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the server.
	 */
	public static void doSend(String channel, PacketByteBuf data) {
		ClientPlayNetworkingImpl.doSend(channel, data);
	}

	public interface Listener {

		boolean handle(Minecraft minecraft, ClientPlayNetworkHandler handler, PacketByteBuf data);

	}
}
