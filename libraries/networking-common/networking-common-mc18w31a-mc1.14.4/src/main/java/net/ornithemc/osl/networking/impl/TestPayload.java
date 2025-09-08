package net.ornithemc.osl.networking.impl;

import java.io.IOException;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.Channels;
import net.ornithemc.osl.networking.api.PacketPayload;

public class TestPayload implements PacketPayload {

	public static final Channel CHANNEL = Channels.make("osl", "test");

	private int data = 1;

	public TestPayload() {
	}

	@Override
	public void read(PacketByteBuf buffer) throws IOException {
		data = buffer.readInt();
	}

	@Override
	public void write(PacketByteBuf buffer) throws IOException {
		buffer.writeInt(data);
	}
}
