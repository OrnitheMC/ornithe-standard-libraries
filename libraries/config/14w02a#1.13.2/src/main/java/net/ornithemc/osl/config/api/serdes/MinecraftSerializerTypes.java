package net.ornithemc.osl.config.api.serdes;

import net.minecraft.nbt.NbtCompound;

public class MinecraftSerializerTypes {

	public static final MemorySerializerType<NbtCompound> NETWORK = SerializerTypes.register("network", () -> new NbtCompound());

}
