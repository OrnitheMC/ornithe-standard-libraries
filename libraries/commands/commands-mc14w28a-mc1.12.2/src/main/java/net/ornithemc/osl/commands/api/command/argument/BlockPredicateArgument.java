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

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.BlockPointer;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.property.Property;
import net.minecraft.nbt.NbtCompound;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class BlockPredicateArgument implements ArgumentType<BlockPredicateArgument.Result> {

	private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone[foo=bar]");
	private static final DynamicCommandExceptionType UNKNOWN_BLOCK_EXCEPTION = new DynamicCommandExceptionType(id -> TextComponents.translatable("arguments.block.unknown", id));

	public static BlockPredicateArgument blockPredicate() {
		return new BlockPredicateArgument();
	}

	public static Predicate<BlockPointer> getBlockPredicate(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, BlockPredicateArgument.Result.class).create();
	}

	@Override
	public Result parse(StringReader reader) throws CommandSyntaxException {
		BlockStateParser parser = new BlockStateParser(reader, false, true).parse();

		if (parser.getState() != null) {
			BlockPredicate predicate = new BlockPredicate(
				parser.getState(),
				parser.getProperties().keySet(),
				parser.getNbt()
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
		BlockStateParser parse = new BlockStateParser(reader, false, true);

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
		private final NbtCompound nbt;

		public BlockPredicate(BlockState state, Set<Property<?>> properties, NbtCompound nbt) {
			this.state = state;
			this.properties = properties;
			this.nbt = nbt;
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

			if (this.nbt == null) {
				return true;
			}

			BlockEntity blockEntity = ptr.getBlockEntity();

			if (blockEntity == null) {
				return false;
			}

			return NbtUtils.matches(this.nbt, blockEntity.writeNbt(new NbtCompound()), true);
		}
	}

	@FunctionalInterface
	public interface Result {

		Predicate<BlockPointer> create() throws CommandSyntaxException;

	}
}
