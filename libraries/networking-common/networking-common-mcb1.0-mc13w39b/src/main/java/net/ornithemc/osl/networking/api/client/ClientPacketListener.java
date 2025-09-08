package net.ornithemc.osl.networking.api.client;

import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientNetworkHandler;

import net.ornithemc.osl.networking.api.PacketPayload;

public interface ClientPacketListener<T> {

	/**
	 * Receive incoming data from the server.
	 *  
	 * @return 
	 *  Whether the data is consumed. Should only return {@code false} if the
	 *  data is completely ignored.
	 */
	boolean handle(Minecraft minecraft, ClientNetworkHandler handler, T data);

	@FunctionalInterface
	public interface Payload<T extends PacketPayload> extends ClientPacketListener<T> {
	}

	@FunctionalInterface
	public interface Buffer extends ClientPacketListener<ByteBuffer> {
	}

	@FunctionalInterface
	public interface Bytes extends ClientPacketListener<byte[]> {
	}
}
