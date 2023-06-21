package net.ornithemc.osl.config.api.serdes.config.option;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializerTypes;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class NetworkOptionSerializers {

	private static final Registry<Class<? extends Option>, NetworkOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(SerializerTypes.NETWORK, "network");

	public static <O extends Option, S extends NetworkOptionSerializer<O>> S register(Class<O> optionType, S serializer) {
		return Registries.registerMapping(REGISTRY, optionType, serializer);
	}

	@SuppressWarnings("unchecked")
	public static <O extends Option> NetworkOptionSerializer<O> get(Class<? extends Option> optionType) {
		return (NetworkOptionSerializer<O>)REGISTRY.get(optionType);
	}
}
