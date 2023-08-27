package net.ornithemc.osl.networking.api.client;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;

public final class ClientPlayNetworking {

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener will only be called from the main thread.
	 */
	public static void registerListener(String channel, Listener listener) {
		ClientPlayNetworkingImpl.registerListener(channel, listener);
	}

	/**
	 * Register a listener to receive data from the server through the given channel.
	 * This listener may be called off the main thread.
	 */
	public static void registerListenerAsync(String channel, Listener listener) {
		ClientPlayNetworkingImpl.registerListenerAsync(channel, listener);
	}

	/**
	 * Remove the listener registered to the given channel.
	 */
	public static void unregisterListener(String channel) {
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
	public static boolean canSend(String channel) {
		return ClientPlayNetworkingImpl.canSend(channel);
	}

	/**
	 * Send a packet to the server through the given channel. The writer will
	 * only be called if the channel is open.
	 */
	public static void send(String channel, Consumer<PacketByteBuf> writer) {
		ClientPlayNetworkingImpl.send(channel, writer);
	}

	/**
	 * Send a packet to the server through the given channel.
	 */
	public static void send(String channel, PacketByteBuf data) {
		ClientPlayNetworkingImpl.send(channel, data);
	}

	/**
	 * Send a packet to the server through the given channel, without checking
	 * whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the server.
	 */
	public static void doSend(String channel, Consumer<PacketByteBuf> writer) {
		ClientPlayNetworkingImpl.doSend(channel, writer);
	}

	/**
	 * Send a packet to the server through the given channel, without checking
	 * whether it is open.
	 * USE WITH CAUTION. Careless use of this method could lead to packet and log
	 * spam on the server.
	 */
	public static void doSend(String channel, PacketByteBuf data) {
		ClientPlayNetworkingImpl.doSend(channel, data);
	}

	public interface Listener {

		/**
		 * Receive incoming data from the server.
		 *  
		 * @return 
		 *  Whether the data is consumed. Should only return {@code false} if the
		 *  data is completely ignored.
		 */
		boolean handle(Minecraft minecraft, ClientPlayNetworkHandler handler, PacketByteBuf data);

	}
}
