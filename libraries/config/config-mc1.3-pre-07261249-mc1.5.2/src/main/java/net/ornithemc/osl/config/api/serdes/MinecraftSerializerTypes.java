package net.ornithemc.osl.config.api.serdes;

import java.io.ByteArrayOutputStream;

import net.minecraft.nbt.NbtCompound;

import net.ornithemc.osl.core.api.io.DataStream;

public class MinecraftSerializerTypes {

	public static final MemorySerializerType<NbtCompound> NBT = SerializerTypes.register("nbt", () -> new NbtCompound());
	public static final MemorySerializerType<DataStream> NETWORK = SerializerTypes.register("network", () -> DataStream.output(new ByteArrayOutputStream()));

}
