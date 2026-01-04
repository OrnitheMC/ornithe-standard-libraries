package net.ornithemc.osl.config.api.serdes;

import java.io.IOException;

import net.ornithemc.osl.core.api.io.DataStream;

public interface NetworkSerializer<T> extends Serializer<T, DataStream> {

	@Override
	void serialize(T value, DataStream data) throws IOException;

	@Override
	default DataStream serialize(T value) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	T deserialize(DataStream data) throws IOException;

}
