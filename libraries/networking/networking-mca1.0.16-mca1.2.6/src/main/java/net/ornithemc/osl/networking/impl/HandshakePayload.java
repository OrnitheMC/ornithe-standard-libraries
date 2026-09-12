package net.ornithemc.osl.networking.impl;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;

public class HandshakePayload implements PacketPayload {

	public static final NamespacedIdentifier CHANNEL = Constants.OSL_HANDSHAKE_CHANNEL;

	public byte protocol;
	public Set<NamespacedIdentifier> channels;

	public HandshakePayload() {
	}

	public HandshakePayload(Set<NamespacedIdentifier> channels) {
		this.protocol = Constants.OSL_HANDSHAKE_PROTOCOL;
		this.channels = channels;
	}

	public static HandshakePayload client() {
		return new HandshakePayload(ClientPlayNetworkingImpl.CHANNEL_LISTENERS.keySet());
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException {
		protocol = buffer.readByte();
		channels = new LinkedHashSet<>();

		int channelCount = buffer.readInt();

		for (int i = 0; i < channelCount; i++) {
			channels.add(StringChannelIdentifierParser.fromString(buffer.readString()));
		}
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException {
		buffer.writeByte(protocol);
		buffer.writeInt(channels.size());

		for (NamespacedIdentifier channel : channels) {
			buffer.writeString(StringChannelIdentifierParser.toString(channel));
		}
	}
}
