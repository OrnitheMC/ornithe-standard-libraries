package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;

import net.minecraft.nbt.NbtCompound;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.config.api.serdes.config.option.NbtOptionSerializer;
import net.ornithemc.osl.config.api.serdes.config.option.NbtOptionSerializers;

public class NbtConfigSerializer implements ConfigSerializer<NbtCompound> {

	private static final String NAME    = "config";
	private static final String VERSION = "version";
	private static final String OPTIONS = "options";

	@Override
	public void serialize(Config config, SerializationSettings settings, NbtCompound nbt) throws IOException {
		if (!settings.skipConfigMetadata) {
			nbt.putString(NAME, config.getName());
			nbt.putInt(VERSION, config.getVersion());
		}

		NbtCompound optionsNbt = new NbtCompound();
		nbt.put(OPTIONS, optionsNbt);

		for (OptionGroup group : config.getGroups()) {
			NbtCompound groupNbt = new NbtCompound();
			optionsNbt.put(group.getName(), groupNbt);

			serializeGroup(group, settings, groupNbt);
		}
	}

	private void serializeGroup(OptionGroup group, SerializationSettings settings, NbtCompound nbt) throws IOException {
		for (Option option : group.getOptions()) {
			if (!settings.skipDefaultOptions || !option.isDefault()) {
				serializeOption(option, settings, nbt);
			}
		}
	}

	private <O extends Option> void serializeOption(O option, SerializationSettings settings, NbtCompound nbt) throws IOException {
		NbtOptionSerializer<O> serializer = NbtOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to serialize option " + option);
		} else {
			serializer.serialize(option, settings, nbt);
		}
	}

	@Override
	public void deserialize(Config config, SerializationSettings settings, NbtCompound nbt) throws IOException {
		if (!settings.skipConfigMetadata) {
			String name = nbt.getString(NAME);
			int version = nbt.getInt(VERSION);
		}

		NbtCompound optionsNbt = nbt.getCompound(OPTIONS);

		for (Object key : optionsNbt.getKeys()) {
			String groupName = (String)key; // thanks proguard
			OptionGroup group = config.getGroup(groupName);
			NbtCompound groupNbt = optionsNbt.getCompound(groupName);

			if (group == null) {
				throw new IOException("unknown group " + groupName);
			}

			deserializeGroup(group, settings, groupNbt);
		}
	}

	private void deserializeGroup(OptionGroup group, SerializationSettings settings, NbtCompound nbt) throws IOException {
		for (Object key : nbt.getKeys()) {
			String optionName = (String)key; // thanks proguard
			Option option = group.getOption(optionName);

			if (option == null) {
				throw new IOException("unknown option " + optionName + " in group " + group.getName());
			}

			deserializeOption(option, settings, nbt);
		}
	}

	private <O extends Option> void deserializeOption(O option, SerializationSettings settings, NbtCompound nbt) throws IOException {
		NbtOptionSerializer<O> serializer = NbtOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to deserialize option " + option);
		} else {
			serializer.deserialize(option, settings, nbt);
		}
	}
}
