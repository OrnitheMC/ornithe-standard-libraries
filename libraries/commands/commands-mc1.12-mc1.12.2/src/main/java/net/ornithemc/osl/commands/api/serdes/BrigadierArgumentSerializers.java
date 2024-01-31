package net.ornithemc.osl.commands.api.serdes;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.resource.Identifier;

public class BrigadierArgumentSerializers {

	public static void init() {
		/*ArgumentSerializers.register(new Identifier("brigadier:bool"), BoolArgumentType.class, new EmptyArgumentSerializer<BoolArgumentType>(BoolArgumentType::bool));
		ArgumentSerializers.register(new Identifier("brigadier:float"), FloatArgumentType.class, new FloatArgumentSerializer());
		ArgumentSerializers.register(new Identifier("brigadier:double"), DoubleArgumentType.class, new DoubleArgumentSerializer());
		ArgumentSerializers.register(new Identifier("brigadier:integer"), IntegerArgumentType.class, new IntegerArgumentSerializer());
		ArgumentSerializers.register(new Identifier("brigadier:string"), StringArgumentType.class, new StringArgumentSerializer());*/
	}

	public static byte minMaxFlags(boolean min, boolean max) {
		byte flags = 0;
		if (min)
			flags = (byte)(flags | 1);
		if (max)
			flags = (byte)(flags | 2);
		return flags;
	}

	public static boolean hasMin(byte flags) {
		return (flags & 1) != 0;
	}

	public static boolean hasMax(byte flags) {
		return (flags & 2) != 0;
	}
}
