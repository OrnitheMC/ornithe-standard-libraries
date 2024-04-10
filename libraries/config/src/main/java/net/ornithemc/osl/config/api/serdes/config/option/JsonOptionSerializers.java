package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import net.ornithemc.osl.config.api.config.option.BaseOption;
import net.ornithemc.osl.config.api.config.option.BooleanOption;
import net.ornithemc.osl.config.api.config.option.ByteOption;
import net.ornithemc.osl.config.api.config.option.DoubleOption;
import net.ornithemc.osl.config.api.config.option.FloatOption;
import net.ornithemc.osl.config.api.config.option.IntegerOption;
import net.ornithemc.osl.config.api.config.option.ListOption;
import net.ornithemc.osl.config.api.config.option.LongOption;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.PathOption;
import net.ornithemc.osl.config.api.config.option.ShortOption;
import net.ornithemc.osl.config.api.config.option.StringOption;
import net.ornithemc.osl.config.api.config.option.UuidOption;
import net.ornithemc.osl.config.api.serdes.JsonSerializer;
import net.ornithemc.osl.config.api.serdes.JsonSerializers;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.config.api.serdes.SerializerTypes;
import net.ornithemc.osl.core.api.json.JsonFile;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class JsonOptionSerializers {

	private static final Registry<Class<? extends Option>, JsonOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(SerializerTypes.JSON, "json");

	public static final JsonOptionSerializer<BooleanOption> BOOLEAN = register(Boolean.class       , BooleanOption.class);
	public static final JsonOptionSerializer<ByteOption>    BYTE    = register(Byte.class          , ByteOption.class);
	public static final JsonOptionSerializer<DoubleOption>  DOUBLE  = register(Double.class        , DoubleOption.class);
	public static final JsonOptionSerializer<FloatOption>   FLOAT   = register(Float.class         , FloatOption.class);
	public static final JsonOptionSerializer<IntegerOption> INTEGER = register(Integer.class       , IntegerOption.class);
	public static final JsonOptionSerializer<LongOption>    LONG    = register(Long.class          , LongOption.class);
	public static final JsonOptionSerializer<ShortOption>   SHORT   = register(Short.class         , ShortOption.class);
	public static final JsonOptionSerializer<StringOption>  STRING  = register(String.class        , StringOption.class);
	public static final JsonOptionSerializer<PathOption>    PATH    = register(Path.class          , PathOption.class);
	public static final JsonOptionSerializer<UuidOption>    UUID    = register(java.util.UUID.class, UuidOption.class);

	@SuppressWarnings("rawtypes")
	public static final JsonOptionSerializer<ListOption> LIST = register(ListOption.class, new JsonOptionSerializer<ListOption>() {

		@Override
		@SuppressWarnings("unchecked")
		public void serialize(ListOption option, SerializationSettings settings, JsonFile json) throws IOException {
			Class elementType = option.getElementType();
			JsonSerializer serializer = JsonSerializers.get(elementType);

			json.writeArray(_json -> {
				List<?> value = (List<?>)option.get();

				for (int i = 0; i < value.size(); i++) {
					Object element = value.get(i);

					if (element == null) {
						json.writeNull();
					} else {
						serializer.serialize(element, json);
					}
				}
			});
		}

		@Override
		@SuppressWarnings("unchecked")
		public void deserialize(ListOption option, SerializationSettings settings, JsonFile json) throws IOException {
			Class elementType = option.getElementType();
			JsonSerializer serializer = JsonSerializers.get(elementType);

			json.readArray(_json -> {
				while (json.hasNext()) {
					Object value = serializer.deserialize(json);

					if (value == null) {
						option.add(null);
					} else {
						option.add(value);
					}
				}
			});
		}
	});

	public static <T, O extends BaseOption<T>> JsonOptionSerializer<O> register(Class<T> valueType, Class<O> optionType) {
		return register(optionType, new JsonOptionSerializer<O>() {

			@Override
			public void serialize(O option, SerializationSettings settings, JsonFile json) throws IOException {
				JsonSerializer<T> serializer = JsonSerializers.get(valueType);
				serializer.serialize(option.get(), json);
			}

			@Override
			public void deserialize(O option, SerializationSettings settings, JsonFile json) throws IOException {
				JsonSerializer<T> serializer = JsonSerializers.get(valueType);
				option.set(serializer.deserialize(json));
			}
		});
	}

	public static <O extends Option, S extends JsonOptionSerializer<O>> S register(Class<O> optionType, S serializer) {
		return Registries.registerMapping(REGISTRY, optionType, serializer);
	}

	public static <O extends Option> JsonOptionSerializer<O> get(Class<? extends Option> optionType) {
		return Registries.getMapping(REGISTRY, optionType);
	}
}
