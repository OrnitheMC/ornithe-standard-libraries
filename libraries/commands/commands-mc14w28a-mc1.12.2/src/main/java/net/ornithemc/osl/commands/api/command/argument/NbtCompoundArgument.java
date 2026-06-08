package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.minecraft.nbt.NbtCompound;

import net.ornithemc.osl.text.api.TextComponents;

public class NbtCompoundArgument implements ArgumentType<NbtCompound> {

	private static final Collection<String> EXAMPLES = Arrays.asList("{}", "{foo=bar}");
	public static final DynamicCommandExceptionType INVALID_NBT_EXCEPTION = new DynamicCommandExceptionType(nbt -> TextComponents.translatable("argument.nbt.invalid", nbt));

	private NbtCompoundArgument() {
	}

	public static NbtCompoundArgument nbtCompound() {
		return new NbtCompoundArgument();
	}

	public static <S> NbtCompound getNbtCompound(CommandContext<S> ctx, String arg) {
		return ctx.getArgument(arg, NbtCompound.class);
	}

	@Override
	public NbtCompound parse(StringReader reader) throws CommandSyntaxException {
		return new SnbtParser(reader).readCompound();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
