package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.minecraft.nbt.NbtCompound;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;

public interface NbtOptionSerializer<O extends Option> extends OptionSerializer<O, NbtCompound> {

	void serialize(O option, SerializationSettings settings, NbtCompound nbt) throws IOException;

	void deserialize(O option, SerializationSettings settings, NbtCompound nbt) throws IOException;

}
