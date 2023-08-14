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
import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;
import net.ornithemc.osl.commands.api.BrigadierCommandManager;
import net.ornithemc.osl.commands.api.SuggestionProvider;
import net.ornithemc.osl.commands.impl.interfaces.mixin.IWorld;

public class BlockPosArgument implements ArgumentType<Coordinates> {

	private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5");
	public static final SimpleCommandExceptionType UNLOADED_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.pos.unloaded"));
	public static final SimpleCommandExceptionType OUT_OF_WORLD_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.pos.outofworld"));

	public static BlockPosArgument blockPos() {
		return new BlockPosArgument();
	}

	public static BlockPos getBlockPos(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, Coordinates.class).getBlockPos(ctx.getSource());
	}

	public static BlockPos getLoadedBlockPos(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		BlockPos pos = getBlockPos(ctx, arg);
		if (!ctx.getSource().getWorld().isChunkLoaded(pos)) {
			throw UNLOADED_EXCEPTION.create();
		}
		if (!((IWorld)ctx.getSource().getWorld()).osl$commands$contains(pos)) {
			throw OUT_OF_WORLD_EXCEPTION.create();
		}
		return pos;
	}

	@Override
	public Coordinates parse(StringReader reader) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '^') {
			return LocalCoordinates.parse(reader);
		}
		return GlobalCoordinates.parseInt(reader);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof SuggestionProvider) {
			String s = builder.getRemaining();
			Collection<SuggestionProvider.Coordinate> coordinates = !s.isEmpty() && s.charAt(0) == '^'
				? Collections.singleton(SuggestionProvider.Coordinate.DEFAULT_LOCAL)
				: ((SuggestionProvider)ctx.getSource()).getCoordinates(false);
			return SuggestionProvider.suggestCoordinates(s, coordinates, builder, BrigadierCommandManager.validator(this::parse));
		}
		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
