package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.core.api.io.DataStream;

public interface NetworkOptionSerializer<O extends Option> extends OptionSerializer<O, DataStream> {

	void serialize(O option, SerializationSettings settings, DataStream ds) throws IOException;

	void deserialize(O option, SerializationSettings settings, DataStream ds) throws IOException;

}
