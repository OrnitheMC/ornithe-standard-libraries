package net.ornithemc.osl.networking.impl;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.Channels;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class HandshakePayload implements PacketPayload {

	public static final Channel CHANNEL = Channels.make("osl", "handshake");

	public Set<Channel> channels;

	public HandshakePayload() {
	}

	public HandshakePayload(Set<Channel> channels) {
		this.channels = channels;
	}

	public static HandshakePayload client() {
		return new HandshakePayload(ClientPlayNetworkingImpl.LISTENERS.keySet());
	}

	public static HandshakePayload server() {
		return new HandshakePayload(ServerPlayNetworkingImpl.LISTENERS.keySet());
	}

	@Override
	public void read(PacketByteBuf buffer) throws IOException {
		channels = new LinkedHashSet<>();
		int channelCount = buffer.readInt();

		for (int i = 0; i < channelCount; i++) {
			String namespace = buffer.readString(Channels.MAX_LENGTH_NAMESPACE);
			String path = buffer.readString(Channels.MAX_LENGTH_DESCRIPTION);

			channels.add(Channels.make(namespace, path));
		}
	}

	@Override
	public void write(PacketByteBuf buffer) throws IOException {
		buffer.writeInt(channels.size());

		for (Channel channel : channels) {
			buffer.writeString(channel.getNamespace(), Channels.MAX_LENGTH_NAMESPACE);
			buffer.writeString(channel.getDescription(), Channels.MAX_LENGTH_DESCRIPTION);
		}
	}
}
