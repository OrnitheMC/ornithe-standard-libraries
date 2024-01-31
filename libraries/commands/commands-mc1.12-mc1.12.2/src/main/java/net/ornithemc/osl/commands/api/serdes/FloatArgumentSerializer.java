package net.ornithemc.osl.commands.api.serdes;

import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.network.PacketByteBuf;

public class FloatArgumentSerializer implements ArgumentSerializer<FloatArgumentType> {

	@Override
	public void serialize(FloatArgumentType floatArgumentType, PacketByteBuf packetByteBuf) {
		boolean bl = floatArgumentType.getMinimum() != -3.4028235E38f;
		boolean bl2 = floatArgumentType.getMaximum() != Float.MAX_VALUE;
		packetByteBuf.writeByte(BrigadierArgumentSerializers.minMaxFlags(bl, bl2));
		if (bl) {
			packetByteBuf.writeFloat(floatArgumentType.getMinimum());
		}
		if (bl2) {
			packetByteBuf.writeFloat(floatArgumentType.getMaximum());
		}
	}

	@Override
	public FloatArgumentType deserialize(PacketByteBuf buffer) {
		byte b = buffer.readByte();
		float f = BrigadierArgumentSerializers.hasMin(b) ? buffer.readFloat() : -3.4028235E38f;
		float g = BrigadierArgumentSerializers.hasMax(b) ? buffer.readFloat() : Float.MAX_VALUE;
		return FloatArgumentType.floatArg(f, g);
	}
}
