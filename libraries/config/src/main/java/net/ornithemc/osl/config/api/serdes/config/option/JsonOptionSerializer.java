package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationOptions;
import net.ornithemc.osl.core.api.json.JsonFile;

public interface JsonOptionSerializer<O extends Option> extends OptionSerializer<O, JsonFile> {

	@Override
	void serialize(O option, SerializationOptions options, JsonFile json) throws IOException;

	@Override
	void deserialize(O option, SerializationOptions options, JsonFile json) throws IOException;

}
