package net.ornithemc.osl.commands.api.command.argument.serdes;

import com.mojang.brigadier.arguments.ArgumentType;

import net.ornithemc.osl.networking.api.PacketBuffer;

public interface ArgumentSerializer<T extends ArgumentType<?>> {

	void serialize(T arg, PacketBuffer buffer);

	T deserialize(PacketBuffer buffer);

}
