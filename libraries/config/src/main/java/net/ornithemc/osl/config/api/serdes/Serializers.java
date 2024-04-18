package net.ornithemc.osl.config.api.serdes;

import net.ornithemc.osl.config.api.ConfigRegistries;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;
import net.ornithemc.osl.core.api.registry.RegistryKey;

public class Serializers {

	private static final Registry<SerializerType<?>, Registry<Class<?>, ? extends Serializer<?, ?>>> REGISTRIES = Registries.register(ConfigRegistries.OBJECT_SERIALIZER);

	public static <M, S extends Serializer<?, M>> Registry<Class<?>, S> register(SerializerType<M> type, String name) {
		return Registries.registerMapping(REGISTRIES, type, Registries.register(RegistryKey.of(ConfigRegistries.OBJECT_SERIALIZER, name)));
	}
}
