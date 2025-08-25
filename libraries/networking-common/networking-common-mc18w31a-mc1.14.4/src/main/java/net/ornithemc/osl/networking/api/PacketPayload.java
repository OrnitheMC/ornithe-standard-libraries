package net.ornithemc.osl.networking.api;

import java.io.IOException;

import net.minecraft.network.PacketByteBuf;

public interface PacketPayload {

	void read(PacketByteBuf buffer) throws IOException;

	void write(PacketByteBuf buffer) throws IOException;

}
