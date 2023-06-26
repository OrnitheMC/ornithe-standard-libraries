package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;

public interface ConfigSerializer<M> {

	void serialize(Config config, SerializationSettings settings, M medium) throws IOException;

	void deserialize(Config config, SerializationSettings settings, M medium) throws IOException;

}
