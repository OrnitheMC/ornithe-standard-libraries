package net.ornithemc.osl.commands.api.command.argument.serdes;

import com.mojang.brigadier.arguments.FloatArgumentType;

import net.ornithemc.osl.networking.api.PacketBuffer;

public class FloatArgumentSerializer implements ArgumentSerializer<FloatArgumentType> {

	@Override
	public void serialize(FloatArgumentType arg, PacketBuffer buffer) {
		boolean hasMin = arg.getMinimum() != -Float.MAX_VALUE;
		boolean hasMax = arg.getMaximum() != Float.MAX_VALUE;

		buffer.writeByte(BrigadierArgumentSerializers.minMaxFlags(hasMin, hasMax));

		if (hasMin) 
			buffer.writeFloat(arg.getMinimum());
		if (hasMax)
			buffer.writeFloat(arg.getMaximum());
	}

	@Override
	public FloatArgumentType deserialize(PacketBuffer buffer) {
		byte flags = buffer.readByte();

		boolean hasMin = BrigadierArgumentSerializers.hasMin(flags);
		boolean hasMax = BrigadierArgumentSerializers.hasMax(flags);

		float min = hasMin ? buffer.readFloat() : -Float.MAX_VALUE;
		float max = hasMax ? buffer.readFloat() : Float.MAX_VALUE;

		return FloatArgumentType.floatArg(min, max);
	}
}
