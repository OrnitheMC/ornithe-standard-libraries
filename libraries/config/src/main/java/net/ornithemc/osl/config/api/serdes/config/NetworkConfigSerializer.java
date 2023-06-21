package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.serdes.SerializationOptions;

public class NetworkConfigSerializer implements ConfigSerializer<ByteBuffer> {

	@Override
	public void serialize(Config config, SerializationOptions options, ByteBuffer buffer) throws IOException {
	}

	@Override
	public void deserialize(Config config, SerializationOptions options, ByteBuffer buffer) throws IOException {
	}
}
