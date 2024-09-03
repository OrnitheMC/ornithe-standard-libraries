package net.ornithemc.osl.config.api.serdes;

import java.io.IOException;

import net.minecraft.nbt.NbtElement;

public interface NbtSerializer<T> extends Serializer<T, NbtElement> {

	@Override
	default void serialize(T value, NbtElement nbt) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	NbtElement serialize(T value) throws IOException;

	@Override
	T deserialize(NbtElement nbt) throws IOException;

}
