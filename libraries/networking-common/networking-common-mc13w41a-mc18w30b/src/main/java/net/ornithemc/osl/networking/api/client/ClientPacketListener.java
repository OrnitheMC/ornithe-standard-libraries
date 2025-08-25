package net.ornithemc.osl.networking.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.networking.api.PacketPayload;

public interface ClientPacketListener<T> {

	/**
	 * Receive incoming data from the server.
	 *  
	 * @return 
	 *  Whether the data is consumed. Should only return {@code false} if the
	 *  data is completely ignored.
	 */
	boolean handle(Minecraft minecraft, ClientPlayNetworkHandler handler, T data);

	@FunctionalInterface
	public interface Payload<T extends PacketPayload> extends ClientPacketListener<T> {
	}

	@FunctionalInterface
	public interface Buffer extends ClientPacketListener<PacketByteBuf> {
	}

	@FunctionalInterface
	public interface Bytes extends ClientPacketListener<byte[]> {
	}
}
