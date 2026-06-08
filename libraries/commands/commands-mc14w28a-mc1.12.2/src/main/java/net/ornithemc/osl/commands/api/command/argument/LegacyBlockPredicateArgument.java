package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.block.pattern.BlockPointer;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.property.Property;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class LegacyBlockPredicateArgument implements ArgumentType<LegacyBlockPredicateArgument.Result> {

	private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone foo=bar", "stone 0");
	private static final DynamicCommandExceptionType UNKNOWN_BLOCK_EXCEPTION = new DynamicCommandExceptionType(id -> TextComponents.translatable("arguments.block.unknown", id));

	public static LegacyBlockPredicateArgument blockPredicate() {
		return new LegacyBlockPredicateArgument();
	}

	public static Predicate<BlockPointer> getBlockPredicate(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, LegacyBlockPredicateArgument.Result.class).create();
	}

	@Override
	public Result parse(StringReader reader) throws CommandSyntaxException {
		BlockStateParser parser = new BlockStateParser(reader, true, true).parse();

		if (parser.getState() != null) {
			BlockPredicate predicate = new BlockPredicate(
				parser.getState(),
				parser.getProperties().keySet()
			);

			return () -> predicate;
		} else {
			String id = parser.getId().toString();

			return () -> {
				throw UNKNOWN_BLOCK_EXCEPTION.create(id);
			};
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		StringReader reader = new StringReader(builder.getInput());
		reader.setCursor(builder.getStart());
		BlockStateParser parse = new BlockStateParser(reader, true, true);

		try {
			parse.parse();
		} catch (CommandSyntaxException var6) {
		}

		return parse.addSuggestions(builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	private static class BlockPredicate implements Predicate<BlockPointer> {

		private final BlockState state;
		private final Set<Property<?>> properties;

		public BlockPredicate(BlockState state, Set<Property<?>> properties) {
			this.state = state;
			this.properties = properties;
		}

		@Override
		public boolean test(BlockPointer ptr) {
			BlockState state = ptr.getState();

			if (state.getBlock() != this.state.getBlock()) {
				return false;
			}

			for (Property<?> property : this.properties) {
				if (state.get(property) != this.state.get(property)) {
					return false;
				}
			}

			return true;
		}
	}

	@FunctionalInterface
	public interface Result {

		Predicate<BlockPointer> create() throws CommandSyntaxException;

	}
}
