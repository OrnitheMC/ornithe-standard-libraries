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

public class LegacyBlockStateArgument implements ArgumentType<BlockInput> {

	private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone foo=bar", "stone 0");

	public static LegacyBlockStateArgument block() {
		return new LegacyBlockStateArgument();
	}

	public static BlockInput getBlock(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, BlockInput.class);
	}

	@Override
	public BlockInput parse(StringReader reader) throws CommandSyntaxException {
		BlockStateParser parser = new BlockStateParser(reader, true, false).parse();
		return new BlockInput(parser.getState(), parser.getProperties().keySet(), parser.getNbt());
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		StringReader reader = new StringReader(builder.getInput());
		reader.setCursor(builder.getStart());
		BlockStateParser parser = new BlockStateParser(reader, true, false);

		try {
			parser.parse();
		} catch (CommandSyntaxException e) {
		}

		return parser.addSuggestions(builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
