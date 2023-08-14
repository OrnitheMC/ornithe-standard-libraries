package net.ornithemc.osl.commands.api.serdes;

import java.util.function.Supplier;

import com.mojang.brigadier.arguments.ArgumentType;

import net.minecraft.network.PacketByteBuf;

public class EmptyArgumentSerializer<T extends ArgumentType<?>> implements ArgumentSerializer<T> {

	private final Supplier<T> constructor;

	public EmptyArgumentSerializer(Supplier<T> constructor) {
		this.constructor = constructor;
	}

	@Override
	public void serialize(T arg, PacketByteBuf buffer) {
	}

	@Override
	public T deserialize(PacketByteBuf buffer) {
		return this.constructor.get();
	}
}
