package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.serdes.SerializationOptions;

public interface ConfigSerializer<M> {

	void serialize(Config config, SerializationOptions options, M medium) throws IOException;

	void deserialize(Config config, SerializationOptions options, M medium) throws IOException;

}
