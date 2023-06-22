package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;

import net.minecraft.network.PacketByteBuf;
import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;

public class NetworkConfigSerializer implements ConfigSerializer<PacketByteBuf> {

	@Override
	public void serialize(Config config, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
		
	}

	@Override
	public void deserialize(Config config, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
		
	}
}
