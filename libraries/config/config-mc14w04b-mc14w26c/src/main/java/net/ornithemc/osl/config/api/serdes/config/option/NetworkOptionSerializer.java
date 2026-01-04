package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;

public interface NetworkOptionSerializer<O extends Option> extends OptionSerializer<O, PacketByteBuf> {

	@Override
	default PacketByteBuf serialize(O option, SerializationSettings settings) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	void serialize(O option, SerializationSettings settings, PacketByteBuf buffer) throws IOException;

	@Override
	void deserialize(O option, SerializationSettings settings, PacketByteBuf buffer) throws IOException;

}
