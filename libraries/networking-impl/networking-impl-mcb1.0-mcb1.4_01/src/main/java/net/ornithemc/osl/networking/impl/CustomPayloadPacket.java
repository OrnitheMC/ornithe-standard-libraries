package net.ornithemc.osl.networking.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.interfaces.mixin.INetworkHandler;

public class CustomPayloadPacket extends Packet implements CustomPayloadPacketAccess {

	private String channel;
	private int size;
	private byte[] data;

	public CustomPayloadPacket() {
	}

	public CustomPayloadPacket(NamespacedIdentifier channel, byte[] data) {
		this.channel = StringChannelIdentifierParser.toString(channel);
		this.data = data;
		this.size = data.length;

		if (this.data != null && this.size > Short.MAX_VALUE) {
			throw new IllegalArgumentException("Payload may not be larger than 32k");
		}
	}

	@Override
	public void read(DataInputStream input) throws IOException {
		this.channel = input.readUTF();
		this.size = input.readShort();
		if (this.size > 0 && this.size < Short.MAX_VALUE) {
			this.data = new byte[this.size];
			input.readFully(this.data);
		}
	}

	@Override
	public void write(DataOutputStream output) throws IOException {
		output.writeUTF(this.channel);
		output.writeShort(this.size);
		if (this.data != null) {
			output.write(this.data);
		}
	}

	@Override
	public void handle(PacketHandler handler) {
		if (handler instanceof INetworkHandler) {
			((INetworkHandler)handler).osl$networking$handleCustomPayload(this);
		}
	}

	@Override
	public int getSize() {
		return 2 + this.channel.length() * 2 + 2 + this.data.length;
	}

	@Override
	public NamespacedIdentifier osl$networking$getChannel() {
		return StringChannelIdentifierParser.fromString(this.channel);
	}

	@Override
	public byte[] osl$networking$getData() {
		return this.data;
	}
}
