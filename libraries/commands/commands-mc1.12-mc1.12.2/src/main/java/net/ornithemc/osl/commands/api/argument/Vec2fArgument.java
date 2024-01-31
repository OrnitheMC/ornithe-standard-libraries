package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;
import net.ornithemc.osl.commands.api.BrigadierCommandManager;
import net.ornithemc.osl.commands.api.SuggestionProvider;

public class Vec2fArgument implements ArgumentType<Coordinates> {

	private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "0.1 -0.5", "~1 ~-2");
	public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.pos2d.incomplete", new Object[0]));

	private final boolean centerCorrect;

	public Vec2fArgument(boolean centerCorrect) {
		this.centerCorrect = centerCorrect;
	}

	public static Vec2fArgument vec2f() {
		return new Vec2fArgument(true);
	}

	public static Vec2f getVec2f(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		Vec3d pos = ctx.getArgument(arg, Coordinates.class).getPosition(ctx.getSource());
		return new Vec2f((float)pos.x, (float)pos.z);
	}

	@Override
	public Coordinates parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();

		if (!reader.canRead()) {
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}

		Coordinate x = Coordinate.parseDouble(reader, this.centerCorrect);

		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(cursor);
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}

		reader.skip();
		Coordinate z = Coordinate.parseDouble(reader, this.centerCorrect);

		return new GlobalCoordinates(x, new Coordinate(true, 0.0), z);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof SuggestionProvider) {
			String s = builder.getRemaining();
			Collection<SuggestionProvider.Coordinate> coordinates = !s.isEmpty() && s.charAt(0) == '^'
				? Collections.singleton(SuggestionProvider.Coordinate.DEFAULT_LOCAL)
				: ((SuggestionProvider) ctx.getSource()).getCoordinates(true);
			return SuggestionProvider.suggestHorizontalCoordinates(s, coordinates, builder, BrigadierCommandManager.validator(this::parse));
		}
		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
