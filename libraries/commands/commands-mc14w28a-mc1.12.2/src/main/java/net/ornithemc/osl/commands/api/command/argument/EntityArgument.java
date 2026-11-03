package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.argument.serdes.ArgumentSerializer;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.text.api.TextComponents;

public class EntityArgument implements ArgumentType<EntitySelector> {

	private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "@e", "@e[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498");
	public static final SimpleCommandExceptionType TOO_MANY_ENTITIES_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.toomany"));
	public static final SimpleCommandExceptionType TOO_MANY_PLAYERS_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.player.toomany"));
	public static final SimpleCommandExceptionType NOT_PLAYER_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.player.entities"));
	public static final SimpleCommandExceptionType ENTITY_NOT_FOUND_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.notfound.entity"));
	public static final SimpleCommandExceptionType PLAYER_NOT_FOUND_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.notfound.player"));
	public static final SimpleCommandExceptionType SELECTOR_NOT_ALLOWED_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.selector.not_allowed"));

	private final boolean single;
	private final boolean players;

	protected EntityArgument(boolean single, boolean players) {
		this.single = single;
		this.players = players;
	}

	public static EntityArgument entity() {
		return new EntityArgument(true, false);
	}

	public static Entity getEntity(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, EntitySelector.class).findSingleEntity(ctx.getSource());
	}

	public static EntityArgument entities() {
		return new EntityArgument(false, false);
	}

	public static Collection<? extends Entity> getEntities(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		Collection<? extends Entity> entities = EntityArgument.getOptionalEntities(ctx, arg);

		if (entities.isEmpty()) {
			throw ENTITY_NOT_FOUND_EXCEPTION.create();
		}

		return entities;
	}

	public static Collection<? extends Entity> getOptionalEntities(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, EntitySelector.class).findEntities(ctx.getSource());
	}

	public static Collection<ServerPlayerEntity> getOptionalPlayers(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, EntitySelector.class).findPlayers(ctx.getSource());
	}

	public static EntityArgument player() {
		return new EntityArgument(true, true);
	}

	public static ServerPlayerEntity getPlayer(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, EntitySelector.class).findSinglePlayer(ctx.getSource());
	}

	public static EntityArgument players() {
		return new EntityArgument(false, true);
	}

	public static Collection<ServerPlayerEntity> getPlayers(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		List<ServerPlayerEntity> players = ctx.getArgument(arg, EntitySelector.class).findPlayers(ctx.getSource());

		if (players.isEmpty()) {
			throw PLAYER_NOT_FOUND_EXCEPTION.create();
		}

		return players;
	}

	@Override
	public EntitySelector parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();

		EntitySelectorParser parser = new EntitySelectorParser(reader);
		EntitySelector selector = parser.parse();

		if (selector.getMax() > 1 && this.single) {
			reader.setCursor(cursor);

			if (this.players) {
				throw TOO_MANY_PLAYERS_EXCEPTION.createWithContext(reader);
			} else {
				throw TOO_MANY_ENTITIES_EXCEPTION.createWithContext(reader);
			}
		}

		if (selector.hasNonPlayers() && this.players && !selector.hasSelf()) {
			reader.setCursor(cursor);
			throw NOT_PLAYER_EXCEPTION.createWithContext(reader);
		}

		return selector;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof SharedSuggestionProvider) {
			StringReader reader = new StringReader(builder.getInput());
			reader.setCursor(builder.getStart());

			SharedSuggestionProvider suggestionProvider = (SharedSuggestionProvider) ctx.getSource();
			EntitySelectorParser parser = new EntitySelectorParser(reader, suggestionProvider.hasPermissions(2));

			try {
				parser.parse();
			} catch (CommandSyntaxException e) {
			}

			return parser.addSuggestions(builder, nameSuggestionBuilder -> {
				Collection<String> playerNames = suggestionProvider.getPlayerNames();

				if (!this.players) {
					playerNames.addAll(suggestionProvider.getSelectedEntities());
				}

				SharedSuggestionProvider.suggestMatching(playerNames, nameSuggestionBuilder);
			});
		}

		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public static class Serializer implements ArgumentSerializer<EntityArgument> {

		private static final int FLAG_SINGLE  = 1;
		private static final int FLAG_PLAYERS = 2;

		@Override
		public void serialize(EntityArgument arg, PacketBuffer buffer) {
			byte flags = 0;

			if (arg.single) flags |= FLAG_SINGLE;
			if (arg.players) flags |= FLAG_PLAYERS;

			buffer.writeByte(flags);
		}

		@Override
		public EntityArgument deserialize(PacketBuffer buffer) {
			byte flags = buffer.readByte();

			boolean single = (flags & FLAG_SINGLE) != 0;
			boolean players = (flags & FLAG_PLAYERS) != 0;

			return new EntityArgument(single, players);
		}
	}
}
