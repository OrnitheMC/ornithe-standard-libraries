package net.ornithemc.osl.commands.api.command.argument.serdes;

import com.mojang.brigadier.arguments.DoubleArgumentType;

import net.ornithemc.osl.networking.api.PacketBuffer;

public class DoubleArgumentSerializer implements ArgumentSerializer<DoubleArgumentType> {

	@Override
	public void serialize(DoubleArgumentType arg, PacketBuffer buffer) {
		boolean hasMin = arg.getMinimum() != -Double.MAX_VALUE;
		boolean hasMax = arg.getMaximum() != Double.MAX_VALUE;

		buffer.writeByte(BrigadierArgumentSerializers.minMaxFlags(hasMin, hasMax));

		if (hasMin) 
			buffer.writeDouble(arg.getMinimum());
		if (hasMax)
			buffer.writeDouble(arg.getMaximum());
	}

	@Override
	public DoubleArgumentType deserialize(PacketBuffer buffer) {
		byte flags = buffer.readByte();

		boolean hasMin = BrigadierArgumentSerializers.hasMin(flags);
		boolean hasMax = BrigadierArgumentSerializers.hasMax(flags);

		double min = hasMin ? buffer.readDouble() : -Double.MAX_VALUE;
		double max = hasMax ? buffer.readDouble() : Double.MAX_VALUE;

		return DoubleArgumentType.doubleArg(min, max);
	}
}
