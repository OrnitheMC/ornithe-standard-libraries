package net.ornithemc.osl.config.api.serdes;

import java.io.IOException;

import net.minecraft.network.PacketByteBuf;

public interface NetworkSerializer<T> extends Serializer<T, PacketByteBuf> {

	@Override
	void serialize(T value, PacketByteBuf buffer) throws IOException;

	@Override
	default PacketByteBuf serialize(T value) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	T deserialize(PacketByteBuf buffer) throws IOException;

}
