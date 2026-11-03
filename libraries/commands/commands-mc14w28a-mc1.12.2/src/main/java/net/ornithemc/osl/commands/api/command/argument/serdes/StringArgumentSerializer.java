package net.ornithemc.osl.commands.api.command.argument.serdes;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType.StringType;

import net.ornithemc.osl.networking.api.PacketBuffer;

public class StringArgumentSerializer implements ArgumentSerializer<StringArgumentType> {

	@Override
	public void serialize(StringArgumentType arg, PacketBuffer buffer) {
		buffer.writeEnum(arg.getType());
	}

	@Override
	public StringArgumentType deserialize(PacketBuffer buffer) {
		StringType type = buffer.readEnum(StringType.class);

		switch (type) {
		case SINGLE_WORD:
			return StringArgumentType.word();
		case QUOTABLE_PHRASE:
			return StringArgumentType.string();
		case GREEDY_PHRASE:
			return StringArgumentType.greedyString();
		}

		return StringArgumentType.greedyString();
	}
}
