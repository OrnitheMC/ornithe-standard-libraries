package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.core.api.json.JsonFile;

public interface JsonOptionSerializer<O extends Option> extends OptionSerializer<O, JsonFile> {

	@Override
	default JsonFile serialize(O option, SerializationSettings settings) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	void serialize(O option, SerializationSettings settings, JsonFile json) throws IOException;

	@Override
	void deserialize(O option, SerializationSettings settings, JsonFile json) throws IOException;

}
