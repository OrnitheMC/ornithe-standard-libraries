package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializer;
import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializers;
import net.ornithemc.osl.core.api.json.JsonFile;

public class JsonConfigSerializer implements ConfigSerializer<JsonFile> {

	private static final String NAME    = "config";
	private static final String VERSION = "version";
	private static final String OPTIONS = "options";

	@Override
	public void serialize(Config config, SerializationSettings settings, JsonFile json) throws IOException {
		try {
			json.write();

			json.writer.name(NAME).value(config.getName());
			json.writer.name(VERSION).value((Number)config.getVersion());

			json.writer.name(OPTIONS).beginObject();

			for (OptionGroup group : config.getGroups()) {
				writeGroup(group, settings, json);
			}

			json.writer.endObject();
		} finally {
			json.close();
		}
	}

	private void writeGroup(OptionGroup group, SerializationSettings settings, JsonFile json) throws IOException {
		json.writer.name(group.getName()).beginObject();

		for (Option option : group.getOptions()) {
			writeOption(option, settings, json);
		}

		json.writer.endObject();
	}

	private <O extends Option> void writeOption(O option, SerializationSettings settings, JsonFile json) throws IOException {
		JsonOptionSerializer<O> serializer = JsonOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to serialize option " + option);
		} else {
			json.writer.name(option.getName());
			serializer.serialize(option, settings, json);
		}
	}

	@Override
	public void deserialize(Config config, SerializationSettings settings, JsonFile json) throws IOException {
		try {
			json.read();

			String name = json.reader.nextString();
			int version = json.reader.nextInt(); // TODO: versioning, updating config data between versions

			json.reader.beginObject();

			while (json.reader.hasNext()) {
				String groupName = json.reader.nextName();
				OptionGroup group = config.getGroup(groupName);

				if (group == null) {
					json.reader.skipValue(); // TODO: log this, check if it needs updating from previous config version
				} else {
					readGroup(group, settings, json);
				}
			}

			json.reader.endObject();
		} finally {
			json.close();
		}
	}

	private void readGroup(OptionGroup group, SerializationSettings settings, JsonFile json) throws IOException {
		json.reader.beginObject();

		while (json.reader.hasNext()) {
			String optionName = json.reader.nextName();
			Option option = group.getOption(optionName);

			if (option == null) {
				json.reader.skipValue(); // TODO: log this, check if it needs updating from previous config version
			} else {
				readOption(option, settings, json);
			}
		}

		json.reader.endObject();
	}

	private <O extends Option> void readOption(O option, SerializationSettings settings, JsonFile json) throws IOException {
		JsonOptionSerializer<O> serializer = JsonOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to deserialize option " + option);
		} else {
			serializer.deserialize(option, settings, json);
		}
	}
}
