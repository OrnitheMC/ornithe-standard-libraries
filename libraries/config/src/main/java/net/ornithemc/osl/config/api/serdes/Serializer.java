package net.ornithemc.osl.config.api.serdes;

import java.io.IOException;

public interface Serializer<T, M> {

	void serialize(T value, M medium) throws IOException;

	T deserialize(M medium) throws IOException;

}
