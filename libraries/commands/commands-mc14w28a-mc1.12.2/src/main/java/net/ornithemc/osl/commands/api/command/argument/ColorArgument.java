package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.TextComponents;

public class ColorArgument implements ArgumentType<Formatting> {

	private static final Collection<String> EXAMPLES = Arrays.asList("red", "green");
	public static final DynamicCommandExceptionType INVALID_COLOR_EXCEPTION = new DynamicCommandExceptionType(color -> TextComponents.translatable("argument.color.invalid", color));

	private ColorArgument() {
	}

	public static ColorArgument color() {
		return new ColorArgument();
	}

	public static Formatting getColor(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, Formatting.class);
	}

	@Override
	public Formatting parse(StringReader reader) throws CommandSyntaxException {
		int startCursor = reader.getCursor();

		String name = reader.readUnquotedString();
		Formatting color = Formatting.byName(name);

		if (color == null) {
			reader.setCursor(startCursor);
			throw INVALID_COLOR_EXCEPTION.createWithContext(reader, name);
		}

		return color;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestMatching(Formatting.getNames(true, false), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
