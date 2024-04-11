package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;
import java.nio.file.Path;

import net.ornithemc.osl.config.api.config.option.BaseOption;
import net.ornithemc.osl.config.api.config.option.BooleanOption;
import net.ornithemc.osl.config.api.config.option.ByteOption;
import net.ornithemc.osl.config.api.config.option.DoubleOption;
import net.ornithemc.osl.config.api.config.option.FloatOption;
import net.ornithemc.osl.config.api.config.option.IntegerOption;
import net.ornithemc.osl.config.api.config.option.ListOption;
import net.ornithemc.osl.config.api.config.option.LongOption;
import net.ornithemc.osl.config.api.config.option.MapOption;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.PathOption;
import net.ornithemc.osl.config.api.config.option.ShortOption;
import net.ornithemc.osl.config.api.config.option.StringOption;
import net.ornithemc.osl.config.api.config.option.UuidOption;
import net.ornithemc.osl.config.api.serdes.JsonSerializers;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.config.api.serdes.SerializerTypes;
import net.ornithemc.osl.core.api.json.JsonFile;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class JsonOptionSerializers {

	private static final Registry<Class<? extends Option>, JsonOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(SerializerTypes.JSON, "json");

	public static final JsonOptionSerializer<BooleanOption> BOOLEAN = register(BooleanOption.class, Boolean.class);
	public static final JsonOptionSerializer<ByteOption>    BYTE    = register(ByteOption.class   , Byte.class);
	public static final JsonOptionSerializer<DoubleOption>  DOUBLE  = register(DoubleOption.class , Double.class);
	public static final JsonOptionSerializer<FloatOption>   FLOAT   = register(FloatOption.class  , Float.class);
	public static final JsonOptionSerializer<IntegerOption> INTEGER = register(IntegerOption.class, Integer.class);
	public static final JsonOptionSerializer<LongOption>    LONG    = register(LongOption.class   , Long.class);
	public static final JsonOptionSerializer<ShortOption>   SHORT   = register(ShortOption.class  , Short.class);
	public static final JsonOptionSerializer<StringOption>  STRING  = register(StringOption.class , String.class);
	public static final JsonOptionSerializer<PathOption>    PATH    = register(PathOption.class   , Path.class);
	public static final JsonOptionSerializer<UuidOption>    UUID    = register(UuidOption.class   , java.util.UUID.class);

	@SuppressWarnings("rawtypes")
	public static final JsonOptionSerializer<ListOption> LIST = register(ListOption.class, new JsonOptionSerializer<ListOption>() {

		@Override
		@SuppressWarnings("unchecked")
		public void serialize(ListOption option, SerializationSettings settings, JsonFile json) throws IOException {
			JsonSerializers.Lists.serialize(option, json);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void deserialize(ListOption option, SerializationSettings settings, JsonFile json) throws IOException {
			JsonSerializers.Lists.deserialize(option, json);
		}
	});
	@SuppressWarnings("rawtypes")
	public static final JsonOptionSerializer<MapOption> MAP = register(MapOption.class, new JsonOptionSerializer<MapOption>() {

		@Override
		@SuppressWarnings("unchecked")
		public void serialize(MapOption option, SerializationSettings settings, JsonFile json) throws IOException {
			JsonSerializers.Maps.serialize(option, json);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void deserialize(MapOption option, SerializationSettings settings, JsonFile json) throws IOException {
			JsonSerializers.Maps.deserialize(option, json);
		}
	});

	public static <T, O extends BaseOption<T>> JsonOptionSerializer<O> register(Class<O> optionType, Class<T> valueType) {
		return register(optionType, new JsonOptionSerializer<O>() {

			@Override
			public void serialize(O option, SerializationSettings settings, JsonFile json) throws IOException {
				JsonSerializers.get(valueType).serialize(option.get(), json);
			}

			@Override
			public void deserialize(O option, SerializationSettings settings, JsonFile json) throws IOException {
				option.set(JsonSerializers.get(valueType).deserialize(json));
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
