package net.ornithemc.osl.networking.api.server;

import java.io.IOException;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketPayload;

public interface ServerPacketListener<T> {

	/**
	 * Receive incoming data from the client.
	 */
	void handle(Context ctx, T data) throws IOException;

	@FunctionalInterface
	interface Payload<T extends PacketPayload> extends ServerPacketListener<T> {
	}

	@FunctionalInterface
	interface Buffer extends ServerPacketListener<PacketBuffer> {
	}

	@FunctionalInterface
	interface Bytes extends ServerPacketListener<byte[]> {
	}

	interface Context {

		/**
		 * @return the current MinecraftServer game instance.
		 */
		MinecraftServer server();

		/**
		 * @return the network handler that received the packet.
		 */
		ServerPlayNetworkHandler networkHandler();

		/**
		 * @return the player that received the packet.
		 */
		ServerPlayerEntity player();

		/**
		 * Ensure the packet listener is running on the main thread.
		 */
		void ensureOnMainThread();

	}
}
