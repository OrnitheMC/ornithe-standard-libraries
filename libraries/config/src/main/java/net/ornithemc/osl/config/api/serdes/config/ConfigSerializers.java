package net.ornithemc.osl.config.api.serdes.config;

import net.ornithemc.osl.config.api.ConfigRegistries;
import net.ornithemc.osl.config.api.serdes.SerializerType;
import net.ornithemc.osl.config.api.serdes.SerializerTypes;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class ConfigSerializers {

	private static final Registry<SerializerType<?>, ConfigSerializer<?>> REGISTRY = Registries.register(ConfigRegistries.CONFIG_SERIALIZER);

	public static final JsonConfigSerializer JSON = register(SerializerTypes.JSON , new JsonConfigSerializer());

	public static <M, C extends ConfigSerializer<M>> C register(SerializerType<M> type, C serializer) {
		return Registries.registerMapping(REGISTRY, type, serializer);
	}

	public static <M, C extends ConfigSerializer<M>> C get(SerializerType<M> type) {
		return Registries.getMapping(REGISTRY, type);
	}

	public static <M, C extends ConfigSerializer<M>> SerializerType<M> getType(C serializer) {
		return Registries.getKey(REGISTRY, serializer);
	}
}
