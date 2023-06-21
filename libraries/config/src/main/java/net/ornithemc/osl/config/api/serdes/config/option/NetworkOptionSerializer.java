package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationOptions;

public interface NetworkOptionSerializer<O extends Option> extends OptionSerializer<O, ByteBuffer> {

	@Override
	void serialize(O option, SerializationOptions options, ByteBuffer buffer) throws IOException;

	@Override
	void deserialize(O option, SerializationOptions options, ByteBuffer buffer) throws IOException;

}
