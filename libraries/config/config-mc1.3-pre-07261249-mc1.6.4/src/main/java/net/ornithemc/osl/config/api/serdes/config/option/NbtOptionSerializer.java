package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.minecraft.nbt.NbtElement;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;

public interface NbtOptionSerializer<O extends Option> extends OptionSerializer<O, NbtElement> {

	@Override
	NbtElement serialize(O option, SerializationSettings settings) throws IOException;

	@Override
	default void serialize(O option, SerializationSettings settings, NbtElement nbt) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	void deserialize(O option, SerializationSettings settings, NbtElement nbt) throws IOException;

}
