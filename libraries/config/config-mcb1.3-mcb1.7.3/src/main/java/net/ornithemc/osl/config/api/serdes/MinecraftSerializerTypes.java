package net.ornithemc.osl.config.api.serdes;

import net.minecraft.nbt.NbtElement;

import net.ornithemc.osl.core.api.io.DataStream;

public class MinecraftSerializerTypes {

	public static final MemorySerializerType<NbtElement> NBT     = SerializerTypes.register("nbt"    , () -> null);
	public static final MemorySerializerType<DataStream> NETWORK = SerializerTypes.register("network", () -> null);

}
