package net.ornithemc.osl.config.api.serdes.config.option;

import net.ornithemc.osl.config.api.ConfigRegistries;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializerType;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;
import net.ornithemc.osl.core.api.registry.RegistryKey;

public class OptionSerializers {

	public static final Registry<SerializerType<?>, Registry<Class<? extends Option>, ? extends OptionSerializer<? extends Option, ?>>> REGISTRIES = Registries.register(ConfigRegistries.OPTION_SERIALIZER);

	public static <M, S extends OptionSerializer<? extends Option, M>> Registry<Class<? extends Option>, S> register(SerializerType<M> type, String name) {
		return Registries.registerMapping(REGISTRIES, type, Registries.register(RegistryKey.of(ConfigRegistries.OPTION_SERIALIZER, name)));
	}
}
