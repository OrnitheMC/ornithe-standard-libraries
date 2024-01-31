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
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.commands.api.SuggestionProvider;
import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;

public class ObjectiveArgument implements ArgumentType<String> {

	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "*", "012");
	private static final DynamicCommandExceptionType NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(objective -> (Message)new TranslatableText("arguments.objective.notFound", objective));
	private static final DynamicCommandExceptionType READ_ONLY_EXCEPTION = new DynamicCommandExceptionType(objective -> (Message)new TranslatableText("arguments.objective.readonly", objective));
	public static final DynamicCommandExceptionType NAME_TOO_LONG_EXCEPTION = new DynamicCommandExceptionType(objective -> (Message)new TranslatableText("commands.scoreboard.objectives.add.longName", objective));

	public static ObjectiveArgument objective() {
		return new ObjectiveArgument();
	}

	public static ScoreboardObjective getObjective(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		String name = ctx.getArgument(arg, String.class);
		Scoreboard scoreboard = ctx.getSource().getWorld().getScoreboard();
		ScoreboardObjective objective = scoreboard.getObjective(name);

		if (objective == null) {
			throw NOT_FOUND_EXCEPTION.create(name);
		}

		return objective;
	}

	public static ScoreboardObjective getWritableObjective(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		ScoreboardObjective objective = getObjective(ctx, arg);

		if (objective.getCriterion().isReadOnly()) {
			throw READ_ONLY_EXCEPTION.create(objective.getName());
		}

		return objective;
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();
		String name = reader.readUnquotedString();

		if (name.length() > 16) {
			reader.setCursor(cursor);
			throw NAME_TOO_LONG_EXCEPTION.createWithContext(reader, 16);
		}

		return name;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof AbstractCommandSourceStack) {
			return SuggestionProvider.suggestMatching(((AbstractCommandSourceStack<?>)ctx.getSource()).getWorld().getScoreboard().getObjectives().stream().map(ScoreboardObjective::getName), builder);
		}
		if (ctx.getSource() instanceof SuggestionProvider) {
			SuggestionProvider suggestionProvider = (SuggestionProvider)ctx.getSource();
			return suggestionProvider.suggest((CommandContext<SuggestionProvider>)ctx, builder);
		}
		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
