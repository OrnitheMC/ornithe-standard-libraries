package net.ornithemc.osl.commands.api.command.argument.serdes;

import java.util.function.Supplier;

import com.mojang.brigadier.arguments.ArgumentType;

import net.ornithemc.osl.networking.api.PacketBuffer;

public class EmptyArgumentSerializer<T extends ArgumentType<?>> implements ArgumentSerializer<T> {

	private final Supplier<T> constructor;

	public EmptyArgumentSerializer(Supplier<T> constructor) {
		this.constructor = constructor;
	}

	@Override
	public void serialize(T arg, PacketBuffer buffer) {
	}

	@Override
	public T deserialize(PacketBuffer buffer) {
		return this.constructor.get();
	}
}
