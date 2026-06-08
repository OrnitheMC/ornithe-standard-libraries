package net.ornithemc.osl.commands.api.command.argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.argument.serdes.ArgumentSerializer;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.text.api.TextComponents;

public class ScoreHolderArgument implements ArgumentType<ScoreHolderArgument.Result> {

	public static final SuggestionProvider<CommandSourceStack> SCORE_HOLDER_SUGGESTIONS = (ctx, builder) -> {
		StringReader reader = new StringReader(builder.getInput());
		reader.setCursor(builder.getStart());
		EntitySelectorParser parser = new EntitySelectorParser(reader);

		try {
			parser.parse();
		} catch (CommandSyntaxException var5) {
		}

		return parser.addSuggestions(builder, _builder -> SharedSuggestionProvider.suggestMatching(ctx.getSource().getPlayerNames(), _builder));
	};
	private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "*", "@e");
	private static final SimpleCommandExceptionType EMPTY_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.scoreHolder.empty"));

	private final boolean multiple;

	public ScoreHolderArgument(boolean multiple) {
		this.multiple = multiple;
	}

	public static ScoreHolderArgument scoreHolder() {
		return new ScoreHolderArgument(false);
	}

	public static ScoreHolderArgument scoreHolders() {
		return new ScoreHolderArgument(true);
	}

	public static String getScoreHolder(CommandContext<CommandSourceStack> context, String arg) throws CommandSyntaxException {
		return getScoreHolders(context, arg).iterator().next();
	}

	public static Collection<String> getScoreHolders(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return getScoreHolders(ctx, arg, Collections::emptyList);
	}

	public static Collection<String> getScoreHoldersOrAll(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return getScoreHolders(ctx, arg, ctx.getSource().getWorld().getScoreboard()::getScoreOwners);
	}

	public static Collection<String> getScoreHolders(CommandContext<CommandSourceStack> ctx, String arg, Supplier<Collection<String>> fallback) throws CommandSyntaxException {
		Collection<String> names = ctx.getArgument(arg, Result.class).create(ctx.getSource(), fallback);

		if (names.isEmpty()) {
			throw EntityArgument.ENTITY_NOT_FOUND_EXCEPTION.create();
		} else {
			return names;
		}
	}

	@Override
	public Result parse(StringReader reader) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '@') {
			EntitySelectorParser parser = new EntitySelectorParser(reader);
			EntitySelector selector = parser.parse();

			if (!this.multiple && selector.getMax() > 1) {
				throw EntityArgument.TOO_MANY_ENTITIES_EXCEPTION.create();
			}

			return new SelectorResult(selector);
		} else {
			int startCursor = reader.getCursor();

			while (reader.canRead() && reader.peek() != ' ') {
				reader.skip();
			}

			String name = reader.getString().substring(startCursor, reader.getCursor());

			if (name.equals("*")) {
				return (source, fallback) -> {
					Collection<String> names = fallback.get();

					if (names.isEmpty()) {
						throw EMPTY_EXCEPTION.create();
					}

					return names;
				};
			} else {
				Collection<String> names = Collections.singleton(name);
				return (source, fallback) -> names;
			}
		}
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	@FunctionalInterface
	public interface Result {

		Collection<String> create(CommandSourceStack source, Supplier<Collection<String>> fallback) throws CommandSyntaxException;

	}

	public static class SelectorResult implements Result {

		private final EntitySelector selector;

		public SelectorResult(EntitySelector selector) {
			this.selector = selector;
		}

		@Override
		public Collection<String> create(CommandSourceStack source, Supplier<Collection<String>> fallback) throws CommandSyntaxException {
			List<? extends Entity> entities = this.selector.findEntities(source);

			if (entities.isEmpty()) {
				throw EntityArgument.ENTITY_NOT_FOUND_EXCEPTION.create();
			}

			List<String> names = new ArrayList<>();

			for (Entity entity : entities) {
				names.add(entity instanceof PlayerEntity
					? entity.getName()
					: entity.getUuidString());
			}

			return names;
		}
	}

	public static class Serializer implements ArgumentSerializer<ScoreHolderArgument> {

		private static final int FLAG_MULTIPLE = 1;

		@Override
		public void serialize(ScoreHolderArgument arg, PacketBuffer buffer) {
			byte flags = 0;

			if (arg.multiple) {
				flags |= FLAG_MULTIPLE;
			}

			buffer.writeByte(flags);
		}

		@Override
		public ScoreHolderArgument deserialize(PacketBuffer buffer) {
			byte flags = buffer.readByte();
			boolean multiple = (flags & FLAG_MULTIPLE) != 0;

			return new ScoreHolderArgument(multiple);
		}
	}
}
