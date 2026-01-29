package net.ornithemc.osl.networking.api.client;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;

import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketPayload;

public interface ClientPacketListener<T> {

	/**
	 * Receive incoming data from the server.
	 */
	void handle(Context ctx, T data) throws IOException;

	@FunctionalInterface
	interface Payload<T extends PacketPayload> extends ClientPacketListener<T> {
	}

	@FunctionalInterface
	interface Buffer extends ClientPacketListener<PacketBuffer> {
	}

	@FunctionalInterface
	interface Bytes extends ClientPacketListener<byte[]> {
	}

	interface Context {

		/**
		 * @return the current Minecraft game instance.
		 */
		Minecraft minecraft();

		/**
		 * @return the network handler that received the packet.
		 */
		ClientPlayNetworkHandler networkHandler();

		/**
		 * Ensure the packet listener is running on the main thread.
		 */
		void ensureOnMainThread();

	}
}
