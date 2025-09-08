package net.ornithemc.osl.networking.api.server;

import java.nio.ByteBuffer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.networking.api.PacketPayload;

public interface ServerPacketListener<T> {

	/**
	 * Receive incoming data from the client.
	 *  
	 * @return 
	 *  Whether the data is consumed. Should only return {@code false} if the
	 *  data is completely ignored.
	 */
	boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, T data);

	@FunctionalInterface
	public interface Payload<T extends PacketPayload> extends ServerPacketListener<T> {
	}

	@FunctionalInterface
	public interface Buffer extends ServerPacketListener<ByteBuffer> {
	}

	@FunctionalInterface
	public interface Bytes extends ServerPacketListener<byte[]> {
	}
}
