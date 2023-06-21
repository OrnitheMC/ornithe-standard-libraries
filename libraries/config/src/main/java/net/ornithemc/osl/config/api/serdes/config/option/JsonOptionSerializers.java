package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.StringOption;
import net.ornithemc.osl.config.api.serdes.SerializationOptions;
import net.ornithemc.osl.config.api.serdes.SerializerTypes;
import net.ornithemc.osl.core.api.json.JsonFile;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class JsonOptionSerializers {

	private static final Registry<Class<? extends Option>, JsonOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(SerializerTypes.JSON, "json");

	public static final JsonOptionSerializer<StringOption> STRING = register(StringOption.class, new JsonOptionSerializer<StringOption>() {

		@Override
		public void serialize(StringOption option, SerializationOptions options, JsonFile json) throws IOException {
			json.writer.value(option.get());
		}

		@Override
		public void deserialize(StringOption option, SerializationOptions options, JsonFile json) throws IOException {
			option.set(json.reader.nextString());
		}
	});

	public static <O extends Option, S extends JsonOptionSerializer<O>> S register(Class<O> optionType, S serializer) {
		return Registries.registerMapping(REGISTRY, optionType, serializer);
	}

	@SuppressWarnings("unchecked")
	public static <O extends Option> JsonOptionSerializer<O> get(Class<? extends Option> optionType) {
		return (JsonOptionSerializer<O>)REGISTRY.get(optionType);
	}
}
