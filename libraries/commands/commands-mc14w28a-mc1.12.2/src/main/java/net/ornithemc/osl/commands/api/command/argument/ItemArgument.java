package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class ItemArgument implements ArgumentType<ItemInput> {

	private static final Collection<String> EXAMPLES = Arrays.asList("stick", "minecraft:stick", "stick{foo=bar}");

	public static ItemArgument item() {
		return new ItemArgument();
	}

	public static <S> ItemInput getItem(CommandContext<S> ctx, String arg) {
		return ctx.getArgument(arg, ItemInput.class);
	}

	@Override
	public ItemInput parse(StringReader reader) throws CommandSyntaxException {
		ItemParser parser = new ItemParser(reader, false).parse();
		return new ItemInput(parser.getItem(), parser.getNbt());
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		StringReader reader = new StringReader(builder.getInput());
		reader.setCursor(builder.getStart());
		ItemParser parser = new ItemParser(reader, false);

		try {
			parser.parse();
		} catch (CommandSyntaxException var6) {
		}

		return parser.addSuggestions(builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
