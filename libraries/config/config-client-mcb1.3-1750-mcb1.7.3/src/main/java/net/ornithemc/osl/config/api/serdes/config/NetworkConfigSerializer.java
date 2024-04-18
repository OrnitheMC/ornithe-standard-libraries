package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.config.api.serdes.config.option.NetworkOptionSerializer;
import net.ornithemc.osl.config.api.serdes.config.option.NetworkOptionSerializers;
import net.ornithemc.osl.core.api.io.DataStream;

public class NetworkConfigSerializer implements ConfigSerializer<DataStream> {

	@Override
	public void serialize(Config config, SerializationSettings settings, DataStream data) throws IOException {
		if (!settings.skipConfigMetadata) {
			data.writeUTF(config.getName());
			data.writeInt(config.getVersion());
		}

		data.writeInt(config.getGroups().size());

		for (OptionGroup group : config.getGroups()) {
			data.writeUTF(group.getName());
			serializeGroup(group, settings, data);
		}
	}

	private void serializeGroup(OptionGroup group, SerializationSettings settings, DataStream data) throws IOException {
		List<Option> options = new ArrayList<>();

		for (Option option : group.getOptions()) {
			if (!settings.skipDefaultOptions || !option.isDefault()) {
				options.add(option);
			}
		}

		data.writeInt(options.size());

		for (Option option : options) {
			data.writeUTF(option.getName());
			serializeOption(option, settings, data);
		}
	}

	private <O extends Option> void serializeOption(O option, SerializationSettings settings, DataStream data) throws IOException {
		NetworkOptionSerializer<O> serializer = NetworkOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to serialize option " + option);
		} else {
			serializer.serialize(option, settings, data);
		}
	}

	@Override
	public void deserialize(Config config, SerializationSettings settings, DataStream data) throws IOException {
		if (!settings.skipConfigMetadata) {
			String name = data.readUTF();
			int version = data.readInt();
		}

		int groupCount = data.readInt();

		for (int i = 0; i < groupCount; i++) {
			String groupName = data.readUTF();
			OptionGroup group = config.getGroup(groupName);

			if (group == null) {
				throw new IOException("unknown group " + groupName);
			}

			deserializeGroup(group, settings, data);
		}
	}

	private void deserializeGroup(OptionGroup group, SerializationSettings settings, DataStream data) throws IOException {
		int optionCount = data.readInt();

		for (int i = 0; i < optionCount; i++) {
			String optionName = data.readUTF();
			Option option = group.getOption(optionName);

			if (option == null) {
				throw new IOException("unknown option " + optionName + " in group " + group.getName());
			}

			deserializeOption(option, settings, data);
		}
	}

	private <O extends Option> void deserializeOption(O option, SerializationSettings settings, DataStream data) throws IOException {
		NetworkOptionSerializer<O> serializer = NetworkOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to deserialize option " + option);
		} else {
			serializer.deserialize(option, settings, data);
		}
	}
}
