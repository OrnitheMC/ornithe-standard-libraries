package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;

public interface NetworkOptionSerializer<O extends Option> extends OptionSerializer<O, ByteBuffer> {

	@Override
	void serialize(O option, SerializationSettings settings, ByteBuffer buffer) throws IOException;

	@Override
	void deserialize(O option, SerializationSettings settings, ByteBuffer buffer) throws IOException;

}
