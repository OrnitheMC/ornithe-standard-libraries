package net.ornithemc.osl.networking.impl;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.access.PacketHandlerAccess;

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
		this.channel = _readString(input, StringChannelIdentifierParser.MAX_LENGTH);
		this.size = input.readShort();
		if (this.size > 0 && this.size < Short.MAX_VALUE) {
			this.data = new byte[this.size];
			input.readFully(this.data);
		}
	}

	@Override
	public void write(DataOutputStream output) throws IOException {
		_writeString(this.channel, output);
		output.writeShort(this.size);
		if (this.data != null) {
			output.write(this.data);
		}
	}

	@Override
	public void handle(PacketHandler handler) {
		if (handler instanceof PacketHandlerAccess) {
			((PacketHandlerAccess) handler).osl$networking$handleCustomPayload(this);
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

	private static String _readString(DataInput input, int maxLength) throws IOException {
		int length = input.readShort();

		if (length > maxLength) {
			throw new IOException("Received string length longer than maximum allowed (" + length + " > " + maxLength + ")");
		}
		if (length < 0) {
			throw new IOException("Received string length is less than zero! Weird string!");
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			sb.append(input.readChar());
		}

		return sb.toString();
	}

	private static void _writeString(String s, DataOutput output) throws IOException {
		if (s.length() > Short.MAX_VALUE) {
			throw new IOException("String too big");
		}

		output.writeShort(s.length());
		output.writeChars(s);
	}
}
