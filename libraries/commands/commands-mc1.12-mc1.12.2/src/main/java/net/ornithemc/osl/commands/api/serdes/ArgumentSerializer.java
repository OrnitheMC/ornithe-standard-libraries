package net.ornithemc.osl.commands.api.serdes;

import com.mojang.brigadier.arguments.ArgumentType;

import net.minecraft.network.PacketByteBuf;

public interface ArgumentSerializer<T extends ArgumentType<?>> {

	void serialize(T arg, PacketByteBuf buffer);

	T deserialize(PacketByteBuf buffer);

}
