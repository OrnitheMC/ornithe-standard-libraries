package net.ornithemc.osl.networking.api;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import net.ornithemc.osl.core.api.util.function.IOConsumer;

public final class PacketBuffers {

	public static PacketBuffer make() {
		return wrapped(Unpooled.buffer());
	}

	public static PacketBuffer make(IOConsumer<PacketBuffer> writer) throws IOException {
		PacketBuffer buffer = make();
		writer.accept(buffer);
		return buffer;
	}

	public static PacketBuffer wrap(byte[] bytes) {
		return wrapped(Unpooled.wrappedBuffer(bytes));
	}

	public static byte[] unwrap(PacketBuffer buffer) {
		byte[] bytes = new byte[buffer.writerIndex()];
		buffer.getBytes(0, bytes);
		return bytes;
	}

	public static PacketBuffer wrapped(ByteBuf buffer) {
		return new PacketBuffer(buffer);
	}
}
