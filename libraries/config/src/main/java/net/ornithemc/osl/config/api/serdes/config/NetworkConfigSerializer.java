package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;

public class NetworkConfigSerializer implements ConfigSerializer<ByteBuffer> {

	@Override
	public void serialize(Config config, SerializationSettings settings, ByteBuffer buffer) throws IOException {
		// TODO
	}

	@Override
	public void deserialize(Config config, SerializationSettings settings, ByteBuffer buffer) throws IOException {
		// TODO
	}
}
