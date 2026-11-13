package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.ornithemc.osl.text.api.TextComponents;

public class RotationArgument implements ArgumentType<Coordinates> {

	private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "~-5 ~5");
	public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.rotation.incomplete"));

	public static RotationArgument rotation() {
		return new RotationArgument();
	}

	public static Coordinates getRotation(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, Coordinates.class);
	}

	@Override
	public Coordinates parse(StringReader reader) throws CommandSyntaxException {
		int startCursor = reader.getCursor();

		Coordinate pitch;
		Coordinate yaw;

		if (!reader.canRead()) {
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}

		pitch = Coordinate.parseDouble(reader, false);

		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(startCursor);
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}

		reader.skip();
		yaw = Coordinate.parseDouble(reader, false);

		return new GlobalCoordinates(yaw, pitch, new Coordinate(true, 0.0));
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
