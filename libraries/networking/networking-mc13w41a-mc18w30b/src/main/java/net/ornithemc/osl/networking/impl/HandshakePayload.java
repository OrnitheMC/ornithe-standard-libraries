package net.ornithemc.osl.networking.impl;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.ChannelIdentifiers;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class HandshakePayload implements PacketPayload {

	public static final NamespacedIdentifier CHANNEL = Constants.OSL_HANDSHAKE_CHANNEL;

	public byte protocol;
	public Set<NamespacedIdentifier> channels;

	public HandshakePayload() {
	}

	public HandshakePayload(Set<NamespacedIdentifier> channels) {
		this.protocol = Constants.OSL_HANDSHAKE_PROTOCOL;
		// we allow registering listeners on channels that do not conform to OSL spec
		// but payloads sent over these channels aren't sent via OSL so we can ignore
		// them for the OSL handshake.
		this.channels = ChannelIdentifiers.dropInvalid(channels);
	}

	public static HandshakePayload client() {
		return new HandshakePayload(ClientPlayNetworkingImpl.CHANNEL_LISTENERS.keySet());
	}

	public static HandshakePayload server() {
		return new HandshakePayload(ServerPlayNetworkingImpl.CHANNEL_LISTENERS.keySet());
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException {
		protocol = buffer.readByte();
		channels = new LinkedHashSet<>();

		int channelCount = buffer.readInt();

		for (int i = 0; i < channelCount; i++) {
			channels.add(buffer.readNamespacedIdentifier());
		}
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException {
		buffer.writeByte(protocol);
		buffer.writeInt(channels.size());

		for (NamespacedIdentifier channel : channels) {
			buffer.writeNamespacedIdentifier(channel);
		}
	}
}
