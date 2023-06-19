package net.ornithemc.osl.config.api.config.serdes;

import net.ornithemc.osl.config.impl.config.serdes.JsonConfigSerializer;
import net.ornithemc.osl.config.impl.config.serdes.NetworkConfigSerializer;

public class ConfigSerializers {

	public static final JsonConfigSerializer JSON = new JsonConfigSerializer();
	public static final NetworkConfigSerializer NETWORK = new NetworkConfigSerializer();

}
