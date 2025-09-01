package net.ornithemc.osl.networking.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.StringChannelParser;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.interfaces.mixin.INetworkHandler;

public class CustomPayloadPacket extends Packet implements CustomPayloadPacketAccess {

	private String channel;
	private int size;
	private byte[] data;

	public CustomPayloadPacket() {
	}

	public CustomPayloadPacket(Channel channel, byte[] data) {
		this.channel = StringChannelParser.toString(channel);
		this.data = data;
		this.size = data.length;

		if (this.data != null && this.size > Short.MAX_VALUE) {
			throw new IllegalArgumentException("Payload may not be larger than 32k");
		}
	}

	// the IOException has been stripped from the read/write methods
	// by the obfuscator, thus we catch it and re-throw it as a
	// runtime exception - it will be caught in Connection#read anyhow

	@Override
	public void read(DataInputStream input) {
		try {
			this.channel = input.readUTF();
			this.size = input.readShort();
			if (this.size > 0 && this.size < Short.MAX_VALUE) {
				this.data = new byte[this.size];
				input.readFully(this.data);
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void write(DataOutputStream output) {
		try {
			output.writeUTF(this.channel);
			output.writeShort(this.size);
			if (this.data != null) {
				output.write(this.data);
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
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
	public Channel osl$networking$getChannel() {
		return StringChannelParser.fromString(this.channel);
	}

	@Override
	public byte[] osl$networking$getData() {
		return this.data;
	}
}
