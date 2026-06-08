package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.scoreboard.ScoreboardScore;
import net.minecraft.util.math.MathHelper;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.text.api.TextComponents;

public class OperationArgument implements ArgumentType<OperationArgument.Operation> {

	private static final Collection<String> EXAMPLES = Arrays.asList("=", ">", "<");
	private static final SimpleCommandExceptionType INVALID_OPERATION_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("arguments.operation.invalid"));
	private static final SimpleCommandExceptionType DIVIDE_BY_ZERO_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("arguments.operation.div0"));

	public static OperationArgument operation() {
		return new OperationArgument();
	}

	public static Operation getOperation(CommandContext<?> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, Operation.class);
	}

	@Override
	public Operation parse(StringReader reader) throws CommandSyntaxException {
		if (!reader.canRead()) {
			throw INVALID_OPERATION_EXCEPTION.create();
		}

		int startCursor = reader.getCursor();

		while (reader.canRead() && reader.peek() != ' ') {
			reader.skip();
		}

		return parseOperation(reader.getString().substring(startCursor, reader.getCursor()));
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestMatching(new String[] { "=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><" }, builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	private static Operation parseOperation(String operation) throws CommandSyntaxException {
		if (operation.equals("><")) {
			return (score1, score2) -> {
				int score = score1.get();
				score1.set(score2.get());
				score2.set(score);
			};
		}

		return parseSimpleOperation(operation);
	}

	private static SimpleOperation parseSimpleOperation(String operation) throws CommandSyntaxException {
		switch (operation) {
		case "=":
			return (score1, score2) -> score2;
		case "+=":
			return (score1, score2) -> score1 + score2;
		case "-=":
			return (score1, score2) -> score1 - score2;
		case "*=":
			return (score1, score2) -> score1 * score2;
		case "/=":
			return (score1, score2) -> {
				if (score2 == 0) {
					throw DIVIDE_BY_ZERO_EXCEPTION.create();
				}

				return MathHelper.floorDiv(score1, score2);
			};
		case "%=":
			return (score1, score2) -> {
				if (score2 == 0) {
					throw DIVIDE_BY_ZERO_EXCEPTION.create();
				}

				return MathHelper.floorMod(score1, score2);
			};
		case "<":
			return Math::min;
		case ">":
			return Math::max;
		}

		throw INVALID_OPERATION_EXCEPTION.create();
	}

	@FunctionalInterface
	public interface Operation {

		void apply(ScoreboardScore score1, ScoreboardScore score2) throws CommandSyntaxException;

	}

	@FunctionalInterface
	interface SimpleOperation extends Operation {

		int apply(int score1, int score2) throws CommandSyntaxException;

		@Override
		default void apply(ScoreboardScore score1, ScoreboardScore score2) throws CommandSyntaxException {
			score1.set(this.apply(score1.get(), score2.get()));
		}
	}
}
