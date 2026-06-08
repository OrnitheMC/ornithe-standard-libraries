package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.ornithemc.osl.commands.api.command.argument.serdes.ArgumentSerializer;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.networking.api.PacketBuffer;

public interface RangeArgument<T extends NumberRange<?>> extends ArgumentType<T> {

	static Ints intRange() {
		return new Ints();
	}

	static NumberRange.Ints getIntRange(CommandContext<CommandSourceStack> ctx, String arg) {
		return ctx.getArgument(arg, NumberRange.Ints.class);
	}

	static Floats floatRange() {
		return new Floats();
	}

	static NumberRange.Floats getFloatRange(CommandContext<CommandSourceStack> ctx, String arg) {
		return ctx.getArgument(arg, NumberRange.Floats.class);
	}

	abstract class Serializer<T extends RangeArgument<?>> implements ArgumentSerializer<T> {

		@Override
		public void serialize(T arg, PacketBuffer buffer) {
		}
	}

	class Ints implements RangeArgument<NumberRange.Ints> {

		private static final Collection<String> EXAMPLES = Arrays.asList("0..5", "0", "-5", "-100..", "..100");

		@Override
		public NumberRange.Ints parse(StringReader reader) throws CommandSyntaxException {
			return NumberRange.Ints.fromString(reader);
		}

		@Override
		public Collection<String> getExamples() {
			return EXAMPLES;
		}

		public static class Serializer extends RangeArgument.Serializer<Ints> {

			@Override
			public Ints deserialize(PacketBuffer buffer) {
				return new Ints();
			}
		}
	}

	class Floats implements RangeArgument<NumberRange.Floats> {

		private static final Collection<String> EXAMPLES = Arrays.asList("0..5.2", "0", "-5.4", "-100.76..", "..100");

		@Override
		public NumberRange.Floats parse(StringReader reader) throws CommandSyntaxException {
			return NumberRange.Floats.fromString(reader);
		}

		@Override
		public Collection<String> getExamples() {
			return EXAMPLES;
		}

		public static class Serializer extends RangeArgument.Serializer<Floats> {

			@Override
			public Floats deserialize(PacketBuffer buffer) {
				return new Floats();
			}
		}
	}
}
