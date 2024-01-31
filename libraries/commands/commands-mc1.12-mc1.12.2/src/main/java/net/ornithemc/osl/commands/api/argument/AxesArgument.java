package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction.Axis;;

public class AxesArgument implements ArgumentType<EnumSet<Axis>> {

	private static final Collection<String> EXAMPLES = Arrays.asList("xyz", "x");
	private static final SimpleCommandExceptionType INVALID_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("arguments.swizzle.invalid"));

	public static AxesArgument axes() {
		return new AxesArgument();
	}

	@SuppressWarnings("unchecked")
	public static EnumSet<Axis> getAxes(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, EnumSet.class);
	}

	@Override
	public EnumSet<Axis> parse(StringReader reader) throws CommandSyntaxException {
		EnumSet<Axis> axes = EnumSet.noneOf(Axis.class);
		while (reader.canRead() && reader.peek() != ' ') {
			Axis axis;
			switch (reader.read()) {
			case 'x':
				axis = Axis.X;
				break;
			case 'y':
				axis = Axis.Y;
				break;
			case 'z':
				axis = Axis.Z;
				break;
			default:
				throw INVALID_EXCEPTION.create();
			}
			if (axes.contains(axis)) {
				throw INVALID_EXCEPTION.create();
			}
			axes.add(axis);
		}
		return axes;
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
