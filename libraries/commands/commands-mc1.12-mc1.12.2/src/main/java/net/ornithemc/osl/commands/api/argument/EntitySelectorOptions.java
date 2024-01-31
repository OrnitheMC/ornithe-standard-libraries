package net.ornithemc.osl.commands.api.argument;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resource.Identifier;
import net.minecraft.scoreboard.team.AbstractTeam;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;

import net.ornithemc.osl.commands.api.SuggestionProvider;

public class EntitySelectorOptions {

	private static final Map<String, Option> OPTIONS = new HashMap<>();
	public static final DynamicCommandExceptionType UNKNOWN_EXCEPTION = new DynamicCommandExceptionType(option -> (Message)new TranslatableText("argument.entity.options.unknown", option));
	public static final DynamicCommandExceptionType NOT_APPLICABLE_EXCEPTION = new DynamicCommandExceptionType(option -> (Message)new TranslatableText("argument.entity.options.inapplicable", option));
	public static final SimpleCommandExceptionType NEGATIVE_DISTANCE_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.entity.options.distance.negative"));
	public static final SimpleCommandExceptionType NEGATIVE_XP_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.entity.options.level.negative"));
	public static final SimpleCommandExceptionType LIMIT_TOO_SMALL_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.entity.options.limit.toosmall"));
	public static final DynamicCommandExceptionType UNKNOWN_ORDER_EXCEPTION = new DynamicCommandExceptionType(order -> (Message)new TranslatableText("argument.entity.options.sort.irreversible", order));
	public static final DynamicCommandExceptionType INVALID_GAME_MODE_EXCEPTION = new DynamicCommandExceptionType(gameMode -> (Message)new TranslatableText("argument.entity.options.mode.invalid", gameMode));
	public static final DynamicCommandExceptionType INVALID_ENTITY_TYPE_EXCEPTION = new DynamicCommandExceptionType(type -> (Message)new TranslatableText("argument.entity.options.type.invalid", type));

	private static void register(String name, Modifier modifier, Predicate<EntitySelectorParser> predicate, Text description) {
		OPTIONS.put(name, new Option(modifier, predicate, description));
	}

	public static void init() {
		if (!OPTIONS.isEmpty()) {
			return;
		}
		EntitySelectorOptions.register("name", parser -> {
			int cursor = parser.getReader().getCursor();
			boolean invert = parser.shouldInvertValue();
			String name = parser.getReader().readString();
			if (parser.hasNotName() && !invert) {
				parser.getReader().setCursor(cursor);
				throw NOT_APPLICABLE_EXCEPTION.createWithContext(parser.getReader(), "name");
			}
			if (invert) {
				parser.setHasNotName(true);
			} else {
				parser.setHasName(true);
			}
			parser.addPredicate(entity -> entity.getName().equals(name) != invert);
		}, parser -> !parser.hasName(), new TranslatableText("argument.entity.options.name.description"));
		EntitySelectorOptions.register("rm", parser -> {
			int cursor = parser.getReader().getCursor();
			float distance = parser.getReader().readFloat();
			if (distance < 0.0F) {
				parser.getReader().setCursor(cursor);
				throw NEGATIVE_DISTANCE_EXCEPTION.createWithContext(parser.getReader());
			}
			if (parser.getMaxDistance() != null && distance > parser.getMaxDistance()) {
				// TODO
			}
			parser.setRestrictOtherWorlds();
			parser.setMinDistance(distance);
		}, parser -> parser.getMinDistance() != null, new TranslatableText("argument.entity.options.distance.min.description"));
		EntitySelectorOptions.register("r", parser -> {
			int cursor = parser.getReader().getCursor();
			float distance = parser.getReader().readFloat();
			if (distance < 0.0F) {
				parser.getReader().setCursor(cursor);
				throw NEGATIVE_DISTANCE_EXCEPTION.createWithContext(parser.getReader());
			}
			if (parser.getMinDistance() != null && distance < parser.getMinDistance()) {
				// TODO
			}
			parser.setRestrictOtherWorlds();
			parser.setMaxDistance(distance);
		}, parser -> parser.getMaxDistance() != null, new TranslatableText("argument.entity.options.distance.max.description"));
		EntitySelectorOptions.register("lm", parser -> {
			int cursor = parser.getReader().getCursor();
			int xp = parser.getReader().readInt();
			if (xp < 0) {
				parser.getReader().setCursor(cursor);
				throw NEGATIVE_XP_EXCEPTION.createWithContext(parser.getReader());
			}
			if (parser.getMaxXp() != null && xp > parser.getMaxXp()) {
				// TODO
			}
			parser.setAllowNonPlayers(false);
			parser.setMinXp(xp);
		}, parser -> parser.getMinXp() != null, new TranslatableText("argument.entity.options.level.min.description"));
		EntitySelectorOptions.register("l", parser -> {
			int cursor = parser.getReader().getCursor();
			int xp = parser.getReader().readInt();
			if (xp < 0) {
				parser.getReader().setCursor(cursor);
				throw NEGATIVE_XP_EXCEPTION.createWithContext(parser.getReader());
			}
			if (parser.getMinXp() != null && xp < parser.getMinXp()) {
				// TODO
			}
			parser.setAllowNonPlayers(false);
			parser.setMaxXp(xp);
		}, parser -> parser.getMinXp() != null, new TranslatableText("argument.entity.options.level.max.description"));
		EntitySelectorOptions.register("x", parser -> {
			parser.setRestrictOtherWorlds();
			parser.setX(parser.getReader().readDouble());
		}, parser -> parser.getX() == null, new TranslatableText("argument.entity.options.x.description"));
		EntitySelectorOptions.register("y", parser -> {
			parser.setRestrictOtherWorlds();
			parser.setY(parser.getReader().readDouble());
		}, parser -> parser.getY() == null, new TranslatableText("argument.entity.options.y.description"));
		EntitySelectorOptions.register("z", parser -> {
			parser.setRestrictOtherWorlds();
			parser.setZ(parser.getReader().readDouble());
		}, parser -> parser.getZ() == null, new TranslatableText("argument.entity.options.z.description"));
		EntitySelectorOptions.register("dx", parser -> {
			parser.setRestrictOtherWorlds();
			parser.setDx(parser.getReader().readDouble());
		}, parser -> parser.getDx() == null, new TranslatableText("argument.entity.options.dx.description"));
		EntitySelectorOptions.register("dy", parser -> {
			parser.setRestrictOtherWorlds();
			parser.setDy(parser.getReader().readDouble());
		}, parser -> parser.getDy() == null, new TranslatableText("argument.entity.options.dy.description"));
		EntitySelectorOptions.register("dz", parser -> {
			parser.setRestrictOtherWorlds();
			parser.setDz(parser.getReader().readDouble());
		}, parser -> parser.getDz() == null, new TranslatableText("argument.entity.options.dz.description"));
		EntitySelectorOptions.register("rxm", parser -> {
			int cursor = parser.getReader().getCursor();
			int pitch = parser.getReader().readInt();
			pitch = (int)MathHelper.wrapDegrees(pitch);
			if (parser.getMaxPitch() != null && pitch > parser.getMaxPitch()) {
				// TODO
			}
			parser.setAllowNonPlayers(false);
			parser.setMinPitch(pitch);
		}, parser -> parser.getMinPitch() != null, new TranslatableText("argument.entity.options.x_rotation.min.description"));
		EntitySelectorOptions.register("rx", parser -> {
			int cursor = parser.getReader().getCursor();
			int pitch = parser.getReader().readInt();
			pitch = (int)MathHelper.wrapDegrees(pitch);
			if (parser.getMinPitch() != null && pitch < parser.getMinPitch()) {
				// TODO
			}
			parser.setAllowNonPlayers(false);
			parser.setMaxPitch(pitch);
		}, parser -> parser.getMaxPitch() != null, new TranslatableText("argument.entity.options.x_rotation.max.description"));
		EntitySelectorOptions.register("rym", parser -> {
			int cursor = parser.getReader().getCursor();
			int yaw = parser.getReader().readInt();
			yaw = (int)MathHelper.wrapDegrees(yaw);
			if (parser.getMaxYaw() != null && yaw > parser.getMaxYaw()) {
				// TODO
			}
			parser.setAllowNonPlayers(false);
			parser.setMinYaw(yaw);
		}, parser -> parser.getMinYaw() != null, new TranslatableText("argument.entity.options.y_rotation.min.description"));
		EntitySelectorOptions.register("ry", parser -> {
			int cursor = parser.getReader().getCursor();
			int yaw = parser.getReader().readInt();
			yaw = (int)MathHelper.wrapDegrees(yaw);
			if (parser.getMinYaw() != null && yaw < parser.getMinYaw()) {
				// TODO
			}
			parser.setAllowNonPlayers(false);
			parser.setMaxYaw(yaw);
		}, parser -> parser.getMaxYaw() != null, new TranslatableText("argument.entity.options.y_rotation.max.description"));
		EntitySelectorOptions.register("c", parser -> {
			int cursor = parser.getReader().getCursor();
			int limit = parser.getReader().readInt();
			if (limit < 1) {
				parser.getReader().setCursor(cursor);
				throw LIMIT_TOO_SMALL_EXCEPTION.createWithContext(parser.getReader());
			}
			parser.setMax(limit);
			parser.setHasLimit(true);
		}, parser -> !parser.hasSelf() && !parser.hasLimit(), new TranslatableText("argument.entity.options.limit.description"));
		EntitySelectorOptions.register("m", parser -> {
			parser.setSuggestions((builder, nameSuggestions) -> {
				String string = builder.getRemaining().toLowerCase(Locale.ROOT);
				boolean invert = !parser.hasNotGameMode();
				boolean bl2 = true;
				if (!string.isEmpty()) {
					if (string.charAt(0) == '!') {
						invert = false;
						string = string.substring(1);
					} else {
						bl2 = false;
					}
				}
				for (GameMode gameMode : GameMode.values()) {
					if (gameMode == GameMode.NOT_SET || !gameMode.getKey().toLowerCase(Locale.ROOT).startsWith(string))
						continue;
					if (bl2) {
						builder.suggest('!' + gameMode.getKey());
					}
					if (invert) {
						builder.suggest(gameMode.getKey());
					}
				}
				return builder.buildFuture();
			});
			int cursor = parser.getReader().getCursor();
			boolean invert = parser.shouldInvertValue();
			if (parser.hasNotGameMode() && !invert) {
				parser.getReader().setCursor(cursor);
				throw NOT_APPLICABLE_EXCEPTION.createWithContext(parser.getReader(), "gamemode");
			}
			String key = parser.getReader().readUnquotedString();
			GameMode gameMode = GameMode.byKeyOrDefault(key, GameMode.NOT_SET);
			if (gameMode == GameMode.NOT_SET) {
				parser.getReader().setCursor(cursor);
				throw INVALID_GAME_MODE_EXCEPTION.createWithContext(parser.getReader(), key);
			}
			parser.setAllowNonPlayers(false);
			parser.addPredicate(entity -> {
				if (!(entity instanceof ServerPlayerEntity)) {
					return false;
				}
				GameMode gameMode2 = ((ServerPlayerEntity) entity).interactionManager.getGameMode();
				return invert ? gameMode2 != gameMode : gameMode2 == gameMode;
			});
			if (invert) {
				parser.setHasNotGameMode(true);
			} else {
				parser.setHasGameMode(true);
			}
		}, parser -> !parser.hasGameMode(), new TranslatableText("argument.entity.options.gamemode.description"));
		EntitySelectorOptions.register("team", parser -> {
			boolean invert = parser.shouldInvertValue();
			String string = parser.getReader().readUnquotedString();
			parser.addPredicate(entity -> {
				if (!(entity instanceof LivingEntity)) {
					return false;
				}
				AbstractTeam abstractTeam = entity.getTeam();
				String string2 = abstractTeam == null ? "" : abstractTeam.getName();
				return string2.equals(string) != invert;
			});
			if (invert) {
				parser.setHasNotTeam(true);
			} else {
				parser.setHasTeam(true);
			}
		}, parser -> !parser.hasTeam(), new TranslatableText("argument.entity.options.team.description"));
		EntitySelectorOptions.register("type", parser -> {
			parser.setSuggestions((builder, nameSuggestions) -> {
				SuggestionProvider.suggestResource(Entities.getIds(), builder, String.valueOf('!'));
				if (!parser.hasNotType()) {
					SuggestionProvider.suggestResource(Entities.getIds(), builder);
				}
				return builder.buildFuture();
			});
			int cursor = parser.getReader().getCursor();
			boolean invert = parser.shouldInvertValue();
			if (parser.hasNotType() && !invert) {
				parser.getReader().setCursor(cursor);
				throw NOT_APPLICABLE_EXCEPTION.createWithContext(parser.getReader(), "type");
			}
			Identifier id = IdentifierParser.parse(parser.getReader());
			if (!Entities.exists(id)) {
				parser.getReader().setCursor(cursor);
				throw INVALID_ENTITY_TYPE_EXCEPTION.createWithContext(parser.getReader(), id.toString());
			}
			Class<? extends Entity> type = Entities.REGISTRY.get(id);
			if (PlayerEntity.class.isAssignableFrom(type) && !invert) {
				parser.setAllowNonPlayers(false);
			}
			parser.addPredicate(entity -> Entities.is(entity, id) != invert);
			if (invert) {
				parser.setHasNotType();
			} else {
				parser.setType(Entities.REGISTRY.get(id));
			}
		}, parser -> !parser.hasType(), new TranslatableText("argument.entity.options.type.description"));
		EntitySelectorOptions.register("nbt", parser -> {
			boolean invert = parser.shouldInvertValue();
			NbtCompound nbt = new SnbtParser(parser.getReader()).readCompound();
			parser.addPredicate(entity -> {
				ItemStack stack;
				NbtCompound entityNbt = entity.writeEntityNbt(new NbtCompound());
				if (entity instanceof PlayerEntity
						&& !(stack = ((PlayerEntity)entity).inventory.getMainHandStack()).isEmpty()) {
					entityNbt.put("SelectedItem", stack.writeNbt(new NbtCompound()));
				}
				return NbtUtils.matches(nbt, entityNbt, true) != invert;
			});
		}, parser -> true, new TranslatableText("argument.entity.options.nbt.description"));
	}

	public static Modifier get(EntitySelectorParser parser, String name, int cursor) throws CommandSyntaxException {
		Option option = OPTIONS.get(name);
		if (option != null) {
			if (option.predicate.test(parser)) {
				return option.modifier;
			}
			throw NOT_APPLICABLE_EXCEPTION.createWithContext(parser.getReader(), name);
		}
		parser.getReader().setCursor(cursor);
		throw UNKNOWN_EXCEPTION.createWithContext(parser.getReader(), name);
	}

	public static void suggest(EntitySelectorParser parser, SuggestionsBuilder builder) {
		String string = builder.getRemaining().toLowerCase(Locale.ROOT);
		for (Map.Entry<String, Option> entry : OPTIONS.entrySet()) {
			if (!entry.getValue().predicate.test(parser) || !entry.getKey().toLowerCase(Locale.ROOT).startsWith(string))
				continue;
			builder.suggest(entry.getKey() + '=', (Message) entry.getValue().description);
		}
	}

	public static class Option {

		public final Modifier modifier;
		public final Predicate<EntitySelectorParser> predicate;
		public final Text description;

		public Option(Modifier modifier, Predicate<EntitySelectorParser> predicate, Text description) {
			this.modifier = modifier;
			this.predicate = predicate;
			this.description = description;
		}
	}

	public interface Modifier {

		void handle(EntitySelectorParser var1) throws CommandSyntaxException;

	}
}
