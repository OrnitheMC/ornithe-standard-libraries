package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;
import net.ornithemc.osl.commands.api.SuggestionProvider;
import net.ornithemc.osl.commands.api.serdes.ArgumentSerializer;

public class EntityArgument implements ArgumentType<EntitySelector> {

	private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "@e", "@e[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498");
	public static final SimpleCommandExceptionType TOO_MANY_ENTITIES_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.entity.toomany"));
	public static final SimpleCommandExceptionType TOO_MANY_PLAYERS_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.player.toomany"));
	public static final SimpleCommandExceptionType NOT_PLAYER_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.player.entities"));
	public static final SimpleCommandExceptionType ENTITY_NOT_FOUND_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.entity.notfound.entity"));
	public static final SimpleCommandExceptionType PLAYER_NOT_FOUND_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.entity.notfound.player"));
	public static final SimpleCommandExceptionType SELECTOR_NOT_ALLOWED_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.entity.selector.not_allowed"));

	private final boolean single;
	private final boolean players;

	protected EntityArgument(boolean single, boolean players) {
		this.single = single;
		this.players = players;
	}

	public static EntityArgument entity() {
		return new EntityArgument(true, false);
	}

	public static Entity getEntity(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, EntitySelector.class).findSingleEntity(ctx.getSource());
	}

	public static EntityArgument entities() {
		return new EntityArgument(false, false);
	}

	public static Collection<? extends Entity> getEntities(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		Collection<? extends Entity> collection = EntityArgument.getOptionalEntities(ctx, arg);
		if (collection.isEmpty()) {
			throw ENTITY_NOT_FOUND_EXCEPTION.create();
		}
		return collection;
	}

	public static Collection<? extends Entity> getOptionalEntities(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, EntitySelector.class).findEntities(ctx.getSource());
	}

	public static Collection<PlayerEntity> getOptionalPlayers(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, EntitySelector.class).findPlayers(ctx.getSource());
	}

	public static EntityArgument player() {
		return new EntityArgument(true, true);
	}

	public static PlayerEntity getPlayer(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, EntitySelector.class).findSinglePlayer(ctx.getSource());
	}

	public static EntityArgument players() {
		return new EntityArgument(false, true);
	}

	public static Collection<PlayerEntity> getPlayers(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		List<PlayerEntity> players = ctx.getArgument(arg, EntitySelector.class).findPlayers(ctx.getSource());
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
		if (ctx.getSource() instanceof SuggestionProvider) {
			StringReader reader = new StringReader(builder.getInput());
			reader.setCursor(builder.getStart());
			SuggestionProvider suggestionProvider = (SuggestionProvider)ctx.getSource();
			EntitySelectorParser parser = new EntitySelectorParser(reader, suggestionProvider.hasPermissions(2));

			try {
				parser.parse();
			} catch (CommandSyntaxException e) {
			}

			return parser.addSuggestions(builder, nameBuilder -> {
				Collection<String> suggestions = suggestionProvider.getPlayerNames();
				if (!this.players) {
					suggestions.addAll(suggestionProvider.getSelectedEntities());
				}
				SuggestionProvider.suggestMatching(suggestions, nameBuilder);
			});
		}
		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public static class Serializer implements ArgumentSerializer<EntityArgument> {

		@Override
		public void serialize(EntityArgument arg, PacketByteBuf buffer) {
			byte flags = 0;
			if (arg.single) {
				flags = (byte)(flags | 1);
			}
			if (arg.players) {
				flags = (byte)(flags | 2);
			}
			buffer.writeByte(flags);
		}

		@Override
		public EntityArgument deserialize(PacketByteBuf buffer) {
			byte flags = buffer.readByte();
			return new EntityArgument((flags & 1) != 0, (flags & 2) != 0);
		}
	}
}
