package net.ornithemc.osl.config.api.serdes.config;

import java.io.IOException;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;
import net.ornithemc.osl.config.api.serdes.NbtType;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.config.api.serdes.config.option.NbtOptionSerializer;
import net.ornithemc.osl.config.api.serdes.config.option.NbtOptionSerializers;
import net.ornithemc.osl.config.impl.interfaces.mixin.INbtCompound;

public class NbtConfigSerializer implements ConfigSerializer<NbtElement> {

	private static final String NAME    = "config";
	private static final String VERSION = "version";
	private static final String OPTIONS = "options";

	@Override
	public void serialize(Config config, SerializationSettings settings, NbtElement nbt) throws IOException {
		NbtCompound configNbt = NbtType.COMPOUND.cast(nbt);

		if (!settings.skipConfigMetadata) {
			configNbt.putString(NAME, config.getName());
			configNbt.putInt(VERSION, config.getVersion());
		}

		configNbt.put(OPTIONS, serializeOptions(config, settings));
	}

	private NbtElement serializeOptions(Config config, SerializationSettings settings) throws IOException {
		NbtCompound optionsNbt = new NbtCompound();

		for (OptionGroup group : config.getGroups()) {
			optionsNbt.put(group.getName(), serializeGroup(group, settings));
		}

		return optionsNbt;
	}

	private NbtElement serializeGroup(OptionGroup group, SerializationSettings settings) throws IOException {
		NbtCompound groupNbt = new NbtCompound();

		for (Option option : group.getOptions()) {
			if (!settings.skipDefaultOptions || !option.isDefault()) {
				groupNbt.put(option.getName(), serializeOption(option, settings));
			}
		}

		return groupNbt;
	}

	private <O extends Option> NbtElement serializeOption(O option, SerializationSettings settings) throws IOException {
		NbtOptionSerializer<O> serializer = NbtOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to serialize option " + option);
		} else {
			return serializer.serialize(option, settings);
		}
	}

	@Override
	public void deserialize(Config config, SerializationSettings settings, NbtElement nbt) throws IOException {
		NbtCompound configNbt = NbtType.COMPOUND.cast(nbt);

		if (!settings.skipConfigMetadata) {
			String name = configNbt.getString(NAME);
			int version = configNbt.getInt(VERSION);
		}

		deserializeOptions(config, settings, configNbt.getCompound(OPTIONS));
	}

	private void deserializeOptions(Config config, SerializationSettings settings, NbtCompound optionsNbt) throws IOException {
		for (String groupName : ((INbtCompound)optionsNbt).osl$config$keySet()) {
			OptionGroup group = config.getGroup(groupName);
			NbtCompound groupNbt = optionsNbt.getCompound(groupName);

			if (group == null) {
				throw new IOException("unknown group " + groupName);
			}

			deserializeGroup(group, settings, groupNbt);
		}
	}

	private void deserializeGroup(OptionGroup group, SerializationSettings settings, NbtCompound groupNbt) throws IOException {
		for (String optionName : ((INbtCompound)groupNbt).osl$config$keySet()) {
			Option option = group.getOption(optionName);

			if (option == null) {
				throw new IOException("unknown option " + optionName + " in group " + group.getName());
			}

			deserializeOption(option, settings, ((INbtCompound)groupNbt).osl$config$get(optionName));
		}
	}

	private <O extends Option> void deserializeOption(O option, SerializationSettings settings, NbtElement nbt) throws IOException {
		NbtOptionSerializer<O> serializer = NbtOptionSerializers.get(option.getClass());

		if (serializer == null) {
			throw new IOException("don't know how to deserialize option " + option);
		} else {
			serializer.deserialize(option, settings, nbt);
		}
	}
}
