package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.text.TranslatableText;

public class RotationArgument implements ArgumentType<Coordinates> {

	private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "~-5 ~5");
	public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.rotation.incomplete"));

	public static RotationArgument rotation() {
		return new RotationArgument();
	}

	public static Coordinates getRotation(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, Coordinates.class);
	}

	@Override
	public Coordinates parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();
		if (!reader.canRead()) {
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		Coordinate y = Coordinate.parseDouble(reader, false);
		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(cursor);
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		reader.skip();
		Coordinate z = Coordinate.parseDouble(reader, false);
		return new GlobalCoordinates(z, y, new Coordinate(true, 0.0));
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
