package net.ornithemc.osl.config.api.serdes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import net.ornithemc.osl.core.api.json.JsonFile;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class JsonSerializers {

	private static final Registry<Class<?>, JsonSerializer<?>> REGISTRY = Serializers.register(SerializerTypes.JSON, "json");

	public static final JsonSerializer<Boolean> BOOLEAN = register(Boolean.class, new JsonSerializer<Boolean>() {

		@Override
		public void serialize(Boolean value, JsonFile json) throws IOException {
			json.writeBoolean(value);
		}

		@Override
		public Boolean deserialize(JsonFile json) throws IOException {
			return json.readBoolean();
		}
	});
	public static final JsonSerializer<Byte> BYTE = register(Byte.class, new JsonSerializer<Byte>() {

		@Override
		public void serialize(Byte value, JsonFile json) throws IOException {
			json.writeNumber(value);
		}

		@Override
		public Byte deserialize(JsonFile json) throws IOException {
			return json.readNumber().byteValue();
		}
	});
	public static final JsonSerializer<Double> DOUBLE = register(Double.class, new JsonSerializer<Double>() {

		@Override
		public void serialize(Double value, JsonFile json) throws IOException {
			json.writeNumber(value);
		}

		@Override
		public Double deserialize(JsonFile json) throws IOException {
			return json.readNumber().doubleValue();
		}
	});
	public static final JsonSerializer<Float> FLOAT = register(Float.class, new JsonSerializer<Float>() {

		@Override
		public void serialize(Float value, JsonFile json) throws IOException {
			json.writeNumber(value);
		}

		@Override
		public Float deserialize(JsonFile json) throws IOException {
			return json.readNumber().floatValue();
		}
	});
	public static final JsonSerializer<Integer> INTEGER = register(Integer.class, new JsonSerializer<Integer>() {

		@Override
		public void serialize(Integer value, JsonFile json) throws IOException {
			json.writeNumber(value);
		}

		@Override
		public Integer deserialize(JsonFile json) throws IOException {
			return json.readNumber().intValue();
		}
	});
	public static final JsonSerializer<Long> LONG = register(Long.class, new JsonSerializer<Long>() {

		@Override
		public void serialize(Long value, JsonFile json) throws IOException {
			json.writeNumber(value);
		}

		@Override
		public Long deserialize(JsonFile json) throws IOException {
			return json.readNumber().longValue();
		}
	});
	public static final JsonSerializer<Short> SHORT = register(Short.class, new JsonSerializer<Short>() {

		@Override
		public void serialize(Short value, JsonFile json) throws IOException {
			json.writeNumber(value);
		}

		@Override
		public Short deserialize(JsonFile json) throws IOException {
			return json.readNumber().shortValue();
		}
	});
	public static final JsonSerializer<String> STRING = register(String.class, new JsonSerializer<String>() {

		@Override
		public void serialize(String value, JsonFile json) throws IOException {
			json.writeString(value);
		}

		@Override
		public String deserialize(JsonFile json) throws IOException {
			return json.readString();
		}
	});
	public static final JsonSerializer<Path> PATH = register(Path.class, new JsonSerializer<Path>() {

		@Override
		public void serialize(Path value, JsonFile json) throws IOException {
			STRING.serialize(value.toAbsolutePath().toString(), json);
		}

		@Override
		public Path deserialize(JsonFile json) throws IOException {
			return Paths.get(STRING.deserialize(json));
		}
	});
	public static final JsonSerializer<UUID> UUID = register(UUID.class, new JsonSerializer<UUID>() {

		@Override
		public void serialize(UUID value, JsonFile json) throws IOException {
			STRING.serialize(value.toString(), json);
		}

		@Override
		public UUID deserialize(JsonFile json) throws IOException {
			return java.util.UUID.fromString(STRING.deserialize(json));
		}
	});

	public static <O, S extends JsonSerializer<O>> S register(Class<O> type, S serializer) {
		return Registries.registerMapping(REGISTRY, type, serializer);
	}

	public static <O> JsonSerializer<O> get(Class<?> type) {
		return Registries.getMapping(REGISTRY, type);
	}
}
