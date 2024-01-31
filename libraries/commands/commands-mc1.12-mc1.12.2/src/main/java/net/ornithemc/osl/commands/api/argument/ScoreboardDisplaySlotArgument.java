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

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.commands.api.SuggestionProvider;

public class ScoreboardDisplaySlotArgument implements ArgumentType<Integer> {

	private static final Collection<String> EXAMPLES = Arrays.asList("sidebar", "foo.bar");
	public static final DynamicCommandExceptionType INVALID_DISPLAY_SLOT_EXCEPTION = new DynamicCommandExceptionType(slot -> (Message)new TranslatableText("argument.scoreboardDisplaySlot.invalid", slot));

	private ScoreboardDisplaySlotArgument() {
	}

	public static ScoreboardDisplaySlotArgument displaySlot() {
		return new ScoreboardDisplaySlotArgument();
	}

	public static int getDisplaySlot(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, Integer.class);
	}

	@Override
	public Integer parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();
		String location = reader.readUnquotedString();
		int slot = Scoreboard.getDisplaySlot(location);

		if (slot == -1) {
			reader.setCursor(cursor);
			throw INVALID_DISPLAY_SLOT_EXCEPTION.createWithContext(reader, location);
		}

		return slot;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SuggestionProvider.suggestMatching(Scoreboard.getDisplayLocations(), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
