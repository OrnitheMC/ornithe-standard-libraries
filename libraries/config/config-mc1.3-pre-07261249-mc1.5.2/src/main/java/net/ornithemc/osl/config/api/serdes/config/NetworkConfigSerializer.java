package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.config.api.serdes.config.option.NetworkOptionSerializer;
import net.ornithemc.osl.config.api.serdes.config.option.NetworkOptionSerializers;
import net.ornithemc.osl.core.api.io.DataStream;

public class NetworkConfigSerializer implements ConfigSerializer<DataStream> {

	@Override
	public void serialize(Config config, SerializationSettings settings, DataStream ds) throws IOException {
		if (!settings.skipConfigMetadata) {
			ds.writeUTF(config.getName());
			ds.writeInt(config.getVersion());
		}

		ds.writeInt(config.getGroups().size());

		for (OptionGroup group : config.getGroups()) {
			ds.writeUTF(group.getName());
			serializeGroup(group, settings, ds);
		}
	}

	private void serializeGroup(OptionGroup group, SerializationSettings settings, DataStream ds) throws IOException {
		for (Option option : group.getOptions()) {
			if (!settings.skipDefaultOptions || !option.isDefault()) {
				ds.writeUTF(option.getName());
				serializeOption(option, settings, ds);
			}
		}
	}

	private <O extends Option> void serializeOption(O option, SerializationSettings settings, DataStream ds) throws IOException {
		NetworkOptionSerializer<O> serializer = NetworkOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to serialize option " + option);
		} else {
			serializer.serialize(option, settings, ds);
		}
	}

	@Override
	public void deserialize(Config config, SerializationSettings settings, DataStream ds) throws IOException {
		if (!settings.skipConfigMetadata) {
			String name = ds.readUTF();
			int version = ds.readInt();
		}

		int groupCount = ds.readInt();

		for (int i = 0; i < groupCount; i++) {
			String groupName = ds.readUTF();
			OptionGroup group = config.getGroup(groupName);

			if (group == null) {
				throw new IOException("unknown group " + groupName);
			}

			deserializeGroup(group, settings, ds);
		}
	}

	private void deserializeGroup(OptionGroup group, SerializationSettings settings, DataStream ds) throws IOException {
		int optionCount = ds.readInt();

		for (int i = 0; i < optionCount; i++) {
			String optionName = ds.readUTF();
			Option option = group.getOption(optionName);

			if (option == null) {
				throw new IOException("unknown option " + optionName + " in group " + group.getName());
			}

			deserializeOption(option, settings, ds);
		}
	}

	private <O extends Option> void deserializeOption(O option, SerializationSettings settings, DataStream ds) throws IOException {
		NetworkOptionSerializer<O> serializer = NetworkOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to deserialize option " + option);
		} else {
			serializer.deserialize(option, settings, ds);
		}
	}
}
