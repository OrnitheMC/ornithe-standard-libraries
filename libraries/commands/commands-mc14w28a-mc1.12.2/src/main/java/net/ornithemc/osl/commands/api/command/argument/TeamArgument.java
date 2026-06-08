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
import net.minecraft.scoreboard.team.Team;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class TeamArgument implements ArgumentType<String> {

	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "123");
	private static final DynamicCommandExceptionType UNKNOWN_TEAM_EXCEPTION = new DynamicCommandExceptionType(team -> TextComponents.translatable("team.notFound", team));

	public static TeamArgument team() {
		return new TeamArgument();
	}

	public static Team getTeam(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		String name = ctx.getArgument(arg, String.class);
		Scoreboard scoreboard = ctx.getSource().getWorld().getScoreboard();
		Team team = scoreboard.getTeam(name);

		if (team == null) {
			throw UNKNOWN_TEAM_EXCEPTION.create(name);
		}

		return team;
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		return reader.readUnquotedString();
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof SharedSuggestionProvider) {
			return SharedSuggestionProvider.suggestMatching(((SharedSuggestionProvider)ctx.getSource()).getTeams(), builder);
		}

		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
