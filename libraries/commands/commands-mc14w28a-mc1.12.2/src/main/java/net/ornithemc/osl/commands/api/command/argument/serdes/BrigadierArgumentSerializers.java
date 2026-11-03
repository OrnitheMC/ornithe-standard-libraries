package net.ornithemc.osl.commands.api.command.argument.serdes;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public final class BrigadierArgumentSerializers {

	private static final int FLAG_MIN = 1;
	private static final int FLAG_MAX = 2;

	public static void init() {
		register("bool", BoolArgumentType.class, new EmptyArgumentSerializer<>(BoolArgumentType::bool));
		register("float", FloatArgumentType.class, new FloatArgumentSerializer());
		register("double", DoubleArgumentType.class, new DoubleArgumentSerializer());
		register("integer", IntegerArgumentType.class, new IntegerArgumentSerializer());
		register("string", StringArgumentType.class, new StringArgumentSerializer());
	}

	private static <T extends ArgumentType<?>> void register(String identifier, Class<T> type, ArgumentSerializer<T> serializer) {
		ArgumentTypes.register(NamespacedIdentifiers.from("brigadier", identifier), type, serializer);
	}

	public static byte minMaxFlags(boolean min, boolean max) {
		byte flags = 0;

		if (min) flags |= FLAG_MIN;
		if (max) flags |= FLAG_MAX;

		return flags;
	}

	public static boolean hasMin(byte flags) {
		return (flags & FLAG_MIN) != 0;
	}

	public static boolean hasMax(byte flags) {
		return (flags & FLAG_MAX) != 0;
	}
}
