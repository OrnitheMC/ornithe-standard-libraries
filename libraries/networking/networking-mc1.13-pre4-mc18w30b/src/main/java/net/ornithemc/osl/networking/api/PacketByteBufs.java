package net.ornithemc.osl.networking.api;

import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import net.minecraft.network.PacketByteBuf;

public final class PacketByteBufs {

	public static PacketByteBuf make() {
		return make(Unpooled.buffer());
	}

	public static PacketByteBuf make(byte[] bytes) {
		return make(Unpooled.wrappedBuffer(bytes));
	}

	public static PacketByteBuf make(ByteBuf buf) {
		return new PacketByteBuf(buf);
	}

	public static PacketByteBuf make(Consumer<PacketByteBuf> writer) {
		PacketByteBuf buffer = make();
		writer.accept(buffer);
		return buffer;
	}
}
