package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;

public interface OptionSerializer<O extends Option, M> {

	M serialize(O option, SerializationSettings settings) throws IOException;

	void serialize(O option, SerializationSettings settings, M medium) throws IOException;

	void deserialize(O option, SerializationSettings settings, M medium) throws IOException;

}
