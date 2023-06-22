package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;

public interface NetworkOptionSerializer<O extends Option> extends OptionSerializer<O, PacketByteBuf> {

	void serialize(O option, SerializationSettings settings, PacketByteBuf buffer) throws IOException;

	void deserialize(O option, SerializationSettings settings, PacketByteBuf buffer) throws IOException;

}
