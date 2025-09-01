package net.ornithemc.osl.networking.api;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.core.api.util.function.IOConsumer;

public final class PacketBuffers {

	public static PacketByteBuf make() {
		return make(Unpooled.buffer());
	}

	public static PacketByteBuf make(byte[] bytes) {
		return make(Unpooled.wrappedBuffer(bytes));
	}

	public static PacketByteBuf make(ByteBuf buffer) {
		return new PacketByteBuf(buffer);
	}

	public static PacketByteBuf make(IOConsumer<PacketByteBuf> writer) throws IOException {
		PacketByteBuf buffer = make();
		writer.accept(buffer);
		return buffer;
	}

	public static PacketByteBuf copy(ByteBuf buffer) {
		return make(buffer.copy());
	}
}
