package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.text.Formatting;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.commands.api.SuggestionProvider;

public class ColorArgument implements ArgumentType<Formatting> {

	private static final Collection<String> EXAMPLES = Arrays.asList("red", "green");
	public static final DynamicCommandExceptionType INVALID_COLOR_EXCEPTION = new DynamicCommandExceptionType(color -> (Message)new TranslatableText("argument.color.invalid", color));

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
		int cursor = reader.getCursor();
		String name = reader.readUnquotedString();
		Formatting formatting = Formatting.byName(name);

		if (formatting == null || formatting.isModifier()) {
			reader.setCursor(cursor);
			throw INVALID_COLOR_EXCEPTION.createWithContext(reader, name);
		}

		return formatting;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SuggestionProvider.suggestMatching(Formatting.getNames(true, false), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
