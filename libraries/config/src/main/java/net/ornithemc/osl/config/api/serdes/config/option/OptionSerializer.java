package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationOptions;

public interface OptionSerializer<O extends Option, M> {

	void serialize(O option, SerializationOptions options, M medium) throws IOException;

	void deserialize(O option, SerializationOptions options, M medium) throws IOException;

}
