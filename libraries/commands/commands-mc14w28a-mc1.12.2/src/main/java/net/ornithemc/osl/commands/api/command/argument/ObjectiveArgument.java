package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class ObjectiveArgument implements ArgumentType<String> {

	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "*", "012");
	private static final DynamicCommandExceptionType NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(objective -> TextComponents.translatable("arguments.objective.notFound", objective));
	private static final DynamicCommandExceptionType READ_ONLY_EXCEPTION = new DynamicCommandExceptionType(objective -> TextComponents.translatable("arguments.objective.readonly", objective));
	public static final DynamicCommandExceptionType NAME_TOO_LONG_EXCEPTION = new DynamicCommandExceptionType(objective -> TextComponents.translatable("commands.scoreboard.objectives.add.longName", objective));

	public static ObjectiveArgument objective() {
		return new ObjectiveArgument();
	}

	public static ScoreboardObjective getObjective(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		String name = ctx.getArgument(arg, String.class);
		Scoreboard scoreboard = ctx.getSource().getWorld().getScoreboard();
		ScoreboardObjective objective = scoreboard.getObjective(name);

		if (objective == null) {
			throw NOT_FOUND_EXCEPTION.create(name);
		}

		return objective;
	}

	public static ScoreboardObjective getWritableObjective(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		ScoreboardObjective objective = getObjective(ctx, arg);

		if (objective.getCriterion().isReadOnly()) {
			throw READ_ONLY_EXCEPTION.create(objective.getName());
		}

		return objective;
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		int startCursor = reader.getCursor();
		String name = reader.readUnquotedString();

		if (name.length() > 16) {
			reader.setCursor(startCursor);
			throw NAME_TOO_LONG_EXCEPTION.createWithContext(reader, 16);
		}

		return name;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof CommandSourceStack) {
			CommandSourceStack source = ((CommandSourceStack) ctx.getSource());
			return SharedSuggestionProvider.suggestMatching(source.getWorld().getScoreboard().getObjectives().stream().map(ScoreboardObjective::getName), builder);
		}

		if (ctx.getSource() instanceof SharedSuggestionProvider) {
			@SuppressWarnings("unchecked")
			CommandContext<SharedSuggestionProvider> context = (CommandContext<SharedSuggestionProvider>) ctx;
			SharedSuggestionProvider suggestionProvider = context.getSource();

			return suggestionProvider.requestSuggestions(context, builder);
		}

		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
