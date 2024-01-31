package net.ornithemc.osl.commands.api.argument;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.text.TranslatableText;
import net.minecraft.world.dimension.DimensionType;

import net.ornithemc.osl.commands.api.SuggestionProvider;

public class DimensionTypeArgument implements ArgumentType<DimensionType> {

	private static final Collection<String> EXAMPLES = Stream.of(DimensionType.OVERWORLD, DimensionType.NETHER).map(dimension -> dimension.getKey()).collect(Collectors.toList());
	public static final DynamicCommandExceptionType INVALID_EXCEPTION = new DynamicCommandExceptionType(dimension -> (Message)new TranslatableText("argument.dimension.invalid", dimension));

	public static DimensionTypeArgument dimension() {
		return new DimensionTypeArgument();
	}

	public static DimensionType getDimension(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, DimensionType.class);
	}

	@Override
	public DimensionType parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();
		String key = reader.readUnquotedString();
		DimensionType dimension = DimensionType.byKey(key);

		if (dimension == null) {
			reader.setCursor(cursor);
			throw INVALID_EXCEPTION.createWithContext(reader, key);
		}

		return dimension;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SuggestionProvider.suggestMatching(Stream.of(DimensionType.values()).map(DimensionType::getKey), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
