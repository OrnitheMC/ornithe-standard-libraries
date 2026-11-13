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

import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.commands.api.Commands;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider.TextCoordinates;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class BlockPosArgument implements ArgumentType<Coordinates> {

	private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5");
	public static final SimpleCommandExceptionType UNLOADED_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.pos.unloaded"));
	public static final SimpleCommandExceptionType OUT_OF_WORLD_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.pos.outofworld"));

	public static BlockPosArgument blockPos() {
		return new BlockPosArgument();
	}

	public static BlockPos getBlockPos(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, Coordinates.class).getBlockPos(ctx.getSource());
	}

	public static BlockPos getLoadedBlockPos(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		BlockPos pos = getBlockPos(ctx, arg);

		if (!ctx.getSource().getWorld().isChunkLoaded(pos)) {
			throw UNLOADED_EXCEPTION.create();
		}
		if (!ctx.getSource().getWorld().contains(pos)) {
			throw OUT_OF_WORLD_EXCEPTION.create();
		}

		return pos;
	}

	@Override
	public Coordinates parse(StringReader reader) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '^') {
			return LocalCoordinates.parse(reader);
		} else {
			return GlobalCoordinates.parseInt(reader);
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof SharedSuggestionProvider) {
			SharedSuggestionProvider source = ((SharedSuggestionProvider) ctx.getSource());
			String prefix = builder.getRemaining();

			Collection<TextCoordinates> coordinates = !prefix.isEmpty() && prefix.charAt(0) == '^'
				? Collections.singleton(TextCoordinates.DEFAULT_LOCAL)
				: source.getRelevantCoordinates(false);

			return SharedSuggestionProvider.suggestCoordinates(prefix, coordinates, builder, Commands.validator(this::parse));
		}

		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
