package net.ornithemc.osl.commands.api.command.argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.GameProfile;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class GameProfileArgument implements ArgumentType<GameProfileArgument.Result> {

	private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "dd12be42-52a9-4a91-a8a1-11c01849e498", "@e");
	public static final SimpleCommandExceptionType UNKNOWN_PLAYER_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.player.unknown"));

	public static Collection<GameProfile> getGameProfiles(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, Result.class).create(ctx.getSource());
	}

	public static GameProfileArgument gameProfiles() {
		return new GameProfileArgument();
	}

	@Override
	public Result parse(StringReader reader) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '@') {
			EntitySelectorParser parser = new EntitySelectorParser(reader);
			EntitySelector selector = parser.parse();

			if (selector.hasNonPlayers()) {
				throw EntityArgument.NOT_PLAYER_EXCEPTION.create();
			} else {
				return new SelectorResult(selector);
			}
		} else {
			int cursor = reader.getCursor();

			while (reader.canRead() && reader.peek() != ' ') {
				reader.skip();
			}

			String name = reader.getString().substring(cursor, reader.getCursor());

			return source -> {
				GameProfile profile = source.getServer().getGameProfileCache().get(name);

				if (profile == null) {
					throw UNKNOWN_PLAYER_EXCEPTION.create();
				} else {
					return Collections.singleton(profile);
				}
			};
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof SharedSuggestionProvider) {
			StringReader reader = new StringReader(builder.getInput());
			reader.setCursor(builder.getStart());
			EntitySelectorParser parser = new EntitySelectorParser(reader);

			try {
				parser.parse();
			} catch (CommandSyntaxException var6) {
			}

			return parser.addSuggestions(builder, suggestionsBuilderx -> SharedSuggestionProvider.suggestMatching(((SharedSuggestionProvider) ctx.getSource()).getPlayerNames(), suggestionsBuilderx));
		} else {
			return Suggestions.empty();
		}
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public static class SelectorResult implements GameProfileArgument.Result {

		private final EntitySelector selector;

		public SelectorResult(EntitySelector selector) {
			this.selector = selector;
		}

		@Override
		public Collection<GameProfile> create(CommandSourceStack source) throws CommandSyntaxException {
			List<ServerPlayerEntity> players = this.selector.findPlayers(source);

			if (players.isEmpty()) {
				throw EntityArgument.PLAYER_NOT_FOUND_EXCEPTION.create();
			}

			List<GameProfile> profiles = new ArrayList<>();

			for (ServerPlayerEntity player : players) {
				profiles.add(player.getGameProfile());
			}

			return profiles;
		}
	}

	@FunctionalInterface
	public interface Result {

		Collection<GameProfile> create(CommandSourceStack source) throws CommandSyntaxException;

	}
}
