package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.block.state.BlockState;

public class LegacyBlockDataArgument implements ArgumentType<BlockState> {

	private static final Collection<String> EXAMPLES = Arrays.asList("foo=bar", "0");

	public static LegacyBlockDataArgument blockState() {
		return new LegacyBlockDataArgument();
	}

	public static BlockState getBlockState(CommandContext<?> ctx, String arg) throws CommandSyntaxException {
        return ctx.getArgument(arg, BlockState.class);
    }

	@Override
	public BlockState parse(StringReader reader) throws CommandSyntaxException {
		return new BlockStateParser(reader, true).parse().getState();
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		StringReader reader = new StringReader(builder.getInput());
		reader.setCursor(builder.getStart());
		BlockStateParser parser = new BlockStateParser(reader, true);
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
