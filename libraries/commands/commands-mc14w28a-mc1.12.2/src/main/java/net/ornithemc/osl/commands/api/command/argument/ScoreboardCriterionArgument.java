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

import net.minecraft.scoreboard.criterion.ScoreboardCriterion;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class ScoreboardCriterionArgument implements ArgumentType<ScoreboardCriterion> {

	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo.bar.baz", "minecraft:foo");
	public static final DynamicCommandExceptionType INVALID_CRITERION_EXCEPTION = new DynamicCommandExceptionType(name -> TextComponents.translatable("argument.criteria.invalid", name));

	private ScoreboardCriterionArgument() {
	}

	public static ScoreboardCriterionArgument criterion() {
		return new ScoreboardCriterionArgument();
	}

	public static ScoreboardCriterion getCriterion(CommandContext<CommandSourceStack> ctx, String arg) {
		return ctx.getArgument(arg, ScoreboardCriterion.class);
	}

	@Override
	public ScoreboardCriterion parse(StringReader reader) throws CommandSyntaxException {
		int startCursor = reader.getCursor();

		while (reader.canRead() && reader.peek() != ' ') {
			reader.skip();
		}

		String name = reader.getString().substring(startCursor, reader.getCursor());
		ScoreboardCriterion criterion = ScoreboardCriterion.BY_NAME.get(name);

		if (criterion == null) {
			reader.setCursor(startCursor);
			throw INVALID_CRITERION_EXCEPTION.createWithContext(reader, name);
		}

		return criterion;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestMatching(ScoreboardCriterion.BY_NAME.keySet(), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
