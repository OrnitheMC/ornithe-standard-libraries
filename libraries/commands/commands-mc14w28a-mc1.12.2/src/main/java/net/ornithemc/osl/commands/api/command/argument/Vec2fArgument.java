package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.Commands;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider.TextCoordinates;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class Vec2fArgument implements ArgumentType<Coordinates> {

	private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "0.1 -0.5", "~1 ~-2");
	public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.pos2d.incomplete"));

	private final boolean centerCorrect;

	public Vec2fArgument(boolean centerCorrect) {
		this.centerCorrect = centerCorrect;
	}

	public static Vec2fArgument vec2f() {
		return new Vec2fArgument(true);
	}

	public static Vec2f getVec2f(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		Vec3d pos = ctx.getArgument(arg, Coordinates.class).getPosition(ctx.getSource());
		return new Vec2f((float) pos.x, (float) pos.z);
	}

	@Override
	public Coordinates parse(StringReader reader) throws CommandSyntaxException {
		int startCursor = reader.getCursor();

		if (!reader.canRead()) {
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}

		Coordinate x;
		Coordinate z;

		x = Coordinate.parseDouble(reader, this.centerCorrect);

		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(startCursor);
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}

		reader.skip();
		z = Coordinate.parseDouble(reader, this.centerCorrect);

		return new GlobalCoordinates(x, new Coordinate(true, 0.0), z);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof SharedSuggestionProvider) {
			SharedSuggestionProvider source = (SharedSuggestionProvider) ctx.getSource();
			String prefix = builder.getRemaining();

			Collection<TextCoordinates> coordinates = !prefix.isEmpty() && prefix.charAt(0) == '^'
				? Collections.singleton(TextCoordinates.DEFAULT_LOCAL)
				: source.getRelevantCoordinates(false);

			return SharedSuggestionProvider.suggestHorizontalCoordinates(prefix, coordinates, builder, Commands.validator(this::parse));
		}

		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
