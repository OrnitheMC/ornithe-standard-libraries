package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;
import net.ornithemc.osl.config.api.serdes.SerializationOptions;
import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializer;
import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializers;
import net.ornithemc.osl.core.api.json.JsonFile;

public class JsonConfigSerializer implements ConfigSerializer<JsonFile> {

	private static final String NAME    = "config";
	private static final String VERSION = "version";
	private static final String OPTIONS = "options";

	@Override
	public void serialize(Config config, SerializationOptions options, JsonFile json) throws IOException {
		try {
			json.write();

			json.writer.name(NAME).value(config.getName());
			json.writer.name(VERSION).value((Number)config.getVersion());

			json.writer.name(OPTIONS).beginObject();

			for (OptionGroup group : config.getGroups()) {
				writeGroup(group, options, json);
			}

			json.writer.endObject();
		} finally {
			json.close();
		}
	}

	private void writeGroup(OptionGroup group, SerializationOptions options, JsonFile json) throws IOException {
		json.writer.name(group.getName()).beginObject();

		for (Option option : group.getOptions()) {
			writeOption(option, options, json);
		}

		json.writer.endObject();
	}

	private <O extends Option> void writeOption(O option, SerializationOptions options, JsonFile json) throws IOException {
		JsonOptionSerializer<O> serializer = JsonOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to serializer option " + option);
		} else {
			json.writer.name(option.getName());
			serializer.serialize(option, options, json);
		}
	}

	@Override
	public void deserialize(Config config, SerializationOptions options, JsonFile json) throws IOException {
		try {
			json.read();

			String name = json.reader.nextString();
			int version = json.reader.nextInt();

			json.reader.beginObject();

			while (json.reader.hasNext()) {
				
			}

			json.reader.endObject();
		} finally {
			json.close();
		}
	}
}
