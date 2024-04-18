package net.ornithemc.osl.config.api.serdes;

import java.io.IOException;

import net.ornithemc.osl.core.api.json.JsonFile;

public interface JsonSerializer<T> extends Serializer<T, JsonFile> {

	@Override
	void serialize(T value, JsonFile json) throws IOException;

	@Override
	default JsonFile serialize(T value) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	T deserialize(JsonFile json) throws IOException;

}
