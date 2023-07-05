package net.ornithemc.osl.config.api.serdes;

import io.netty.buffer.Unpooled;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class MinecraftSerializerTypes {

	public static final MemorySerializerType<NbtCompound> NBT = SerializerTypes.register("nbt", () -> new NbtCompound());
	public static final MemorySerializerType<PacketByteBuf> NETWORK = SerializerTypes.register("network", () -> new PacketByteBuf(Unpooled.buffer()));

}
