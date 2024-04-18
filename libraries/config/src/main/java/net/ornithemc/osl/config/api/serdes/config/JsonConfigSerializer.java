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

			if (!settings.skipConfigMetadata) {
				json.writeString(NAME, config.getName());
				json.writeNumber(VERSION, config.getVersion());
			}

			json.writeObject(OPTIONS, _json -> {
				serializeOptions(config, settings, json);
			});
		} finally {
			json.close();
		}
	}

	private void serializeOptions(Config config, SerializationSettings settings, JsonFile json) throws IOException {
		for (OptionGroup group : config.getGroups()) {
			json.writeObject(group.getName(), __json -> {
				serializeGroup(group, settings, json);
			});
		}
	}

	private void serializeGroup(OptionGroup group, SerializationSettings settings, JsonFile json) throws IOException {
		for (Option option : group.getOptions()) {
			if (!settings.skipDefaultOptions || !option.isDefault()) {
				json.writeName(option.getName());
				serializeOption(option, settings, json);
			}
		}
	}

	private <O extends Option> void serializeOption(O option, SerializationSettings settings, JsonFile json) throws IOException {
		JsonOptionSerializer<O> serializer = JsonOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to serialize option " + option);
		} else {
			serializer.serialize(option, settings, json);
		}
	}

	@Override
	public void deserialize(Config config, SerializationSettings settings, JsonFile json) throws IOException {
		try {
			json.read();

			if (!settings.skipConfigMetadata) {
				String name = json.readString(NAME);
				int version = json.readInt(VERSION); // TODO: versioning, updating config data between versions
			}

			json.readObject(OPTIONS, _json -> {
				deserializeOptions(config, settings, json);
			});
		} finally {
			json.close();
		}
	}

	private void deserializeOptions(Config config, SerializationSettings settings, JsonFile json) throws IOException {
		while (json.hasNext()) {
			String groupName = json.readName();
			OptionGroup group = config.getGroup(groupName);

			if (group == null) {
				json.skipValue(); // TODO: log this, check if it needs updating from previous config version
			} else {
				json.readObject(__json -> {
					deserializeGroup(group, settings, json);
				});
			}
		}
	}

	private void deserializeGroup(OptionGroup group, SerializationSettings settings, JsonFile json) throws IOException {
		while (json.hasNext()) {
			String optionName = json.readName();
			Option option = group.getOption(optionName);

			if (option == null) {
				json.skipValue(); // TODO: log this, check if it needs updating from previous config version
			} else {
				deserializeOption(option, settings, json);
			}
		}
	}

	private <O extends Option> void deserializeOption(O option, SerializationSettings settings, JsonFile json) throws IOException {
		JsonOptionSerializer<O> serializer = JsonOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to deserialize option " + option);
		} else {
			serializer.deserialize(option, settings, json);
		}
	}
}
