package net.ornithemc.osl.config.api.serdes;

import java.nio.ByteBuffer;
import java.nio.file.Path;

import net.ornithemc.osl.config.api.ConfigRegistries;
import net.ornithemc.osl.core.api.json.JsonFile;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;
import net.ornithemc.osl.core.api.registry.RegistryKey;

public final class SerializerTypes {

	private static final Registry<RegistryKey, SerializerType<?>> REGISTRY = Registries.register(ConfigRegistries.SERIALIZER_TYPE);

	public static final FileSerializerType<JsonFile> JSON = register("json" , new FileSerializerType<JsonFile>() {

		@Override
		public JsonFile open(Path path) {
			return new JsonFile(path);
		}
	});
	public static final SerializerType<ByteBuffer> NETWORK = register("network", new SerializerType<ByteBuffer>() { });

	public static <T extends SerializerType<?>> T register(String name, T type) {
		return Registries.registerMapping(REGISTRY, RegistryKey.of(ConfigRegistries.SERIALIZER_TYPE, name), type);
	}

	public static <T extends SerializerType<?>> T get(String name) {
		return get(RegistryKey.of(ConfigRegistries.SERIALIZER_TYPE, name));
	}

	@SuppressWarnings("unchecked")
	public static <T extends SerializerType<?>> T get(RegistryKey key) {
		return (T)REGISTRY.get(key);
	}

	public static RegistryKey getKey(SerializerType<?> type) {
		return REGISTRY.getKey(type);
	}
}
