package net.ornithemc.osl.config.api.serdes.config;

import net.ornithemc.osl.config.api.serdes.MinecraftSerializerTypes;

public class MinecraftConfigSerializers {

	public static final NetworkConfigSerializer NETWORK = ConfigSerializers.register(MinecraftSerializerTypes.NETWORK, new NetworkConfigSerializer());

}
