package net.ornithemc.osl.commands.api.command.argument.serdes;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.ornithemc.osl.networking.api.PacketBuffer;

public class IntegerArgumentSerializer implements ArgumentSerializer<IntegerArgumentType> {

	@Override
	public void serialize(IntegerArgumentType arg, PacketBuffer buffer) {
		boolean hasMin = arg.getMinimum() != Integer.MIN_VALUE;
		boolean hasMax = arg.getMaximum() != Integer.MAX_VALUE;

		buffer.writeByte(BrigadierArgumentSerializers.minMaxFlags(hasMin, hasMax));

		if (hasMin)
			buffer.writeInt(arg.getMinimum());
		if (hasMax)
			buffer.writeInt(arg.getMaximum());
	}

	@Override
	public IntegerArgumentType deserialize(PacketBuffer buffer) {
		byte flags = buffer.readByte();

		boolean hasMin = BrigadierArgumentSerializers.hasMin(flags);
		boolean hasMax = BrigadierArgumentSerializers.hasMax(flags);

		int min = hasMin ? buffer.readInt() : Integer.MIN_VALUE;
		int max = hasMax ? buffer.readInt() : Integer.MAX_VALUE;

		return IntegerArgumentType.integer(min, max);
	}
}
