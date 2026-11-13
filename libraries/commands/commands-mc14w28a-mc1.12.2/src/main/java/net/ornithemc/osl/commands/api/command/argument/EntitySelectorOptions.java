package net.ornithemc.osl.commands.api.command.argument;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancements;
import net.minecraft.advancement.criterion.CriterionProgress;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.Identifier;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.team.AbstractTeam;
import net.minecraft.server.AdvancementManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.entities.api.EntityTypeRegistry;
import net.ornithemc.osl.entities.api.entity.EntityType;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class EntitySelectorOptions {

	private static final Map<String, Option> OPTIONS = new HashMap<>();

	public static final DynamicCommandExceptionType UNKNOWN_EXCEPTION = new DynamicCommandExceptionType(option -> TextComponents.translatable("argument.entity.options.unknown", option));
	public static final DynamicCommandExceptionType NOT_APPLICABLE_EXCEPTION = new DynamicCommandExceptionType(option -> TextComponents.translatable("argument.entity.options.inapplicable", option));
	public static final SimpleCommandExceptionType NEGATIVE_DISTANCE_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.options.distance.negative"));
	public static final SimpleCommandExceptionType NEGATIVE_XP_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.options.level.negative"));
	public static final SimpleCommandExceptionType LIMIT_TOO_SMALL_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.options.limit.toosmall"));
	public static final DynamicCommandExceptionType UNKNOWN_ORDER_EXCEPTION = new DynamicCommandExceptionType(order -> TextComponents.translatable("argument.entity.options.sort.irreversible", order));
	public static final DynamicCommandExceptionType INVALID_GAME_MODE_EXCEPTION = new DynamicCommandExceptionType(gameMode -> TextComponents.translatable("argument.entity.options.mode.invalid", gameMode));
	public static final DynamicCommandExceptionType INVALID_ENTITY_TYPE_EXCEPTION = new DynamicCommandExceptionType(type -> TextComponents.translatable("argument.entity.options.type.invalid", type));

	private static final boolean NULLABLE_ITEM_STACKS = MinecraftVersion.resolve().compareTo("1.10.2") <= 0;
	private static final boolean PARSE_ADVANCMENTS = MinecraftVersion.resolve().compareTo("1.12") >= 0;

	private static void register(String name, Modifier modifier, Predicate<EntitySelectorParser> predicate, TextComponent description) {
		OPTIONS.put(name, new Option(modifier, predicate, description));
	}

	public static void init() {
		if (OPTIONS.isEmpty()) {
			register(
				"name",
				parser -> {
					int startCursor = parser.getReader().getCursor();
					boolean inverted = parser.shouldInvertValue();
					String name = parser.getReader().readString();

					if (parser.hasNotName() && !inverted) {
						parser.getReader().setCursor(startCursor);
						throw NOT_APPLICABLE_EXCEPTION.createWithContext(parser.getReader(), "name");
					} else {
						if (inverted) {
							parser.setHasNotName(true);
						} else {
							parser.setHasName(true);
						}

						parser.addPredicate(entity -> entity.getName().equals(name) != inverted);
					}
				},
				parser -> !parser.hasName(),
				TextComponents.translatable("argument.entity.options.name.description"));
			register(
				"distance",
				parser -> {
					int startCursor = parser.getReader().getCursor();
					NumberRange.Floats range = NumberRange.Floats.fromString(parser.getReader());

					if ((range.getMin() != null && range.getMin() < 0.0F) || (range.getMax() != null && range.getMax() < 0.0F)) {
						parser.getReader().setCursor(startCursor);
						throw NEGATIVE_DISTANCE_EXCEPTION.createWithContext(parser.getReader());
					} else {
						parser.setDistance(range);
						parser.setRestrictOtherWorlds();
					}
				},
				parser -> parser.getDistance().isAny(),
				TextComponents.translatable("argument.entity.options.distance.description"));
			register(
				"level",
				parser -> {
					int startCursor = parser.getReader().getCursor();
					NumberRange.Ints range = NumberRange.Ints.fromString(parser.getReader());

					if ((range.getMin() != null && range.getMin() < 0) || (range.getMax() != null && range.getMax() < 0)) {
						parser.getReader().setCursor(startCursor);
						throw NEGATIVE_XP_EXCEPTION.createWithContext(parser.getReader());
					} else {
						parser.setXp(range);
						parser.setAllowNonPlayers(false);
					}
				},
				parser -> parser.getXp().isAny(),
				TextComponents.translatable("argument.entity.options.level.description"));
			register(
				"x",
				parser -> {
					parser.setRestrictOtherWorlds();
					parser.setX(parser.getReader().readDouble());
				},
				parser -> parser.getX() == null,
				TextComponents.translatable("argument.entity.options.x.description"));
			register(
				"y",
				parser -> {
					parser.setRestrictOtherWorlds();
					parser.setY(parser.getReader().readDouble());
				},
				parser -> parser.getY() == null,
				TextComponents.translatable("argument.entity.options.y.description"));
			register(
				"z",
				parser -> {
					parser.setRestrictOtherWorlds();
					parser.setZ(parser.getReader().readDouble());
				},
				parser -> parser.getZ() == null,
				TextComponents.translatable("argument.entity.options.z.description"));
			register(
				"dx",
				parser -> {
					parser.setRestrictOtherWorlds();
					parser.setDx(parser.getReader().readDouble());
				},
				parser -> parser.getDx() == null,
				TextComponents.translatable("argument.entity.options.dx.description"));
			register(
				"dy",
				parser -> {
					parser.setRestrictOtherWorlds();
					parser.setDy(parser.getReader().readDouble());
				},
				parser -> parser.getDy() == null,
				TextComponents.translatable("argument.entity.options.dy.description"));
			register(
				"dz",
				parser -> {
					parser.setRestrictOtherWorlds();
					parser.setDz(parser.getReader().readDouble());
				},
				parser -> parser.getDz() == null,
				TextComponents.translatable("argument.entity.options.dz.description"));
			register(
				"x_rotation",
				parser -> parser.setPitch(WrappedNumberRange.fromString(parser.getReader(), true, MathHelper::wrapDegrees)),
				parser -> parser.getPitch() == WrappedNumberRange.ANY,
				TextComponents.translatable("argument.entity.options.x_rotation.description"));
			register(
				"y_rotation",
				parser -> parser.setYaw(WrappedNumberRange.fromString(parser.getReader(), true, MathHelper::wrapDegrees)),
				parser -> parser.getYaw() == WrappedNumberRange.ANY,
				TextComponents.translatable("argument.entity.options.y_rotation.description"));
			register(
				"limit",
				parser -> {
					int startCursor = parser.getReader().getCursor();
					int limit = parser.getReader().readInt();

					if (limit < 1) {
						parser.getReader().setCursor(startCursor);
						throw LIMIT_TOO_SMALL_EXCEPTION.createWithContext(parser.getReader());
					} else {
						parser.setMax(limit);
						parser.setHasLimit(true);
					}
				},
				parser -> !parser.hasSelf() && !parser.hasLimit(),
				TextComponents.translatable("argument.entity.options.limit.description"));
			register(
				"sort",
				parser -> {
					int startCursor = parser.getReader().getCursor();
					String orderName = parser.getReader().readUnquotedString();

					parser.setSuggestions(EntitySelectorParser.SORT_ORDER_SUGGESTIONS);

					BiConsumer<Vec3d, List<? extends Entity>> order;

					switch (orderName) {
					case "nearest":
						order = EntitySelectorParser.ORDER_NEAREST;
						break;
					case "furthest":
						order = EntitySelectorParser.ORDER_FURTHEST;
						break;
					case "random":
						order = EntitySelectorParser.ORDER_RANDOM;
						break;
					case "arbitrary":
						order = EntitySelectorParser.ORDER_ARBITRARY;
						break;
					default:
						parser.getReader().setCursor(startCursor);
						throw UNKNOWN_ORDER_EXCEPTION.createWithContext(parser.getReader(), orderName);
					}
	
					parser.setOrder(order);
					parser.setHasOrder(true);
				},
				parser -> !parser.hasSelf() && !parser.hasOrder(),
				TextComponents.translatable("argument.entity.options.sort.description"));
			register(
				"gamemode",
				parser -> {
					parser.setSuggestions((builder, nameSuggestions) -> {
						String prefix = builder.getRemaining().toLowerCase(Locale.ROOT);

						boolean suggestInverted = !prefix.isEmpty() && prefix.charAt(0) == '!';
						boolean suggestKey = !suggestInverted && !parser.hasNotGameMode();

						if (!prefix.isEmpty()) {
							if (prefix.charAt(0) == '!') {
								suggestKey = false;
								prefix = prefix.substring(1);
							} else {
								suggestInverted = false;
							}
						}
	
						for (GameMode gameMode : GameMode.values()) {
							if (gameMode != GameMode.NOT_SET && gameMode.getKey().toLowerCase(Locale.ROOT).startsWith(prefix)) {
								if (suggestInverted) {
									builder.suggest('!' + gameMode.getKey());
								}
	
								if (suggestKey) {
									builder.suggest(gameMode.getKey());
								}
							}
						}
	
						return builder.buildFuture();
					});

					int startCursor = parser.getReader().getCursor();
					boolean inverted = parser.shouldInvertValue();

					if (parser.hasNotGameMode() && !inverted) {
						parser.getReader().setCursor(startCursor);
						throw NOT_APPLICABLE_EXCEPTION.createWithContext(parser.getReader(), "gamemode");
					}

					String key = parser.getReader().readUnquotedString();
					GameMode gameMode = GameMode.byKeyOrDefault(key, GameMode.NOT_SET);

					if (gameMode == GameMode.NOT_SET) {
						parser.getReader().setCursor(startCursor);
						throw INVALID_GAME_MODE_EXCEPTION.createWithContext(parser.getReader(), key);
					}

					parser.setAllowNonPlayers(false);
					parser.addPredicate(entity -> {
						if (entity instanceof ServerPlayerEntity) {
							GameMode playerGameMode = ((ServerPlayerEntity) entity).interactionManager.getGameMode();
							return inverted ? playerGameMode != gameMode : playerGameMode == gameMode;
						} else {
							return false;
						}
					});

					if (inverted) {
						parser.setHasNotGameMode(true);
					} else {
						parser.setHasGameMode(true);
					}
				},
				parser -> !parser.hasGameMode(),
				TextComponents.translatable("argument.entity.options.gamemode.description"));
			register(
				"team",
				parser -> {
					boolean inverted = parser.shouldInvertValue();
					String teamName = parser.getReader().readUnquotedString();

					parser.addPredicate(entity -> {
						if (entity instanceof LivingEntity) {
							AbstractTeam team = entity.getTeam();
							String entityTeamName = team == null ? "" : team.getName();

							return entityTeamName.equals(teamName) != inverted;
						} else {
							return false;
						}
					});

					if (inverted) {
						parser.setHasNotTeam(true);
					} else {
						parser.setHasTeam(true);
					}
				},
				parser -> !parser.hasTeam(),
				TextComponents.translatable("argument.entity.options.team.description"));
			register(
				"type",
				parser -> {
					parser.setSuggestions((builder, nameSuggestions) -> {
						SharedSuggestionProvider.suggestResource(EntityTypeRegistry.identifierSet(), builder, String.valueOf('!'));

						if (!parser.hasNotType()) {
							SharedSuggestionProvider.suggestResource(EntityTypeRegistry.identifierSet(), builder);
						}
	
						return builder.buildFuture();
					});

					int startCursor = parser.getReader().getCursor();
					boolean inverted = parser.shouldInvertValue();

					if (parser.hasNotType() && !inverted) {
						parser.getReader().setCursor(startCursor);
						throw NOT_APPLICABLE_EXCEPTION.createWithContext(parser.getReader(), "type");
					}

					NamespacedIdentifier key = IdentifierParser.parse(parser.getReader());
					EntityType<?> type = EntityTypeRegistry.getEntityType(key);

					if (type == null) {
						parser.getReader().setCursor(startCursor);
						throw INVALID_ENTITY_TYPE_EXCEPTION.createWithContext(parser.getReader(), key.toString());
					}

					if (type.getType() == PlayerEntity.class && !inverted) {
						parser.setAllowNonPlayers(false);
					}

					parser.addPredicate(entity -> (type.getType() == entity.getClass()) != inverted);

					if (inverted) {
						parser.setHasNotType();
					} else {
						parser.setType(type.getType());
					}
				},
				parser -> !parser.hasType(),
				TextComponents.translatable("argument.entity.options.type.description"));
			register(
				"tag",
				parser -> {
					boolean inverted = parser.shouldInvertValue();
					String tagName = parser.getReader().readUnquotedString();

					parser.addPredicate(entity -> "".equals(tagName)
						? entity.getCommandTags().isEmpty() != inverted
						: entity.getCommandTags().contains(tagName) != inverted);
				},
				parser -> true,
				TextComponents.translatable("argument.entity.options.tag.description"));
			register(
				"nbt",
				parser -> {
					boolean inverted = parser.shouldInvertValue();
					NbtCompound nbt = new SnbtParser(parser.getReader()).readCompound();

					parser.addPredicate(entity -> {
						NbtCompound entityNbt = entity.writeNbtWithoutId(new NbtCompound());

						if (entity instanceof ServerPlayerEntity) {
							ItemStack selectedItem = ((ServerPlayerEntity) entity).inventory.getSelectedItem();

							if (NULLABLE_ITEM_STACKS ? (selectedItem != null) : !selectedItem.isEmpty()) {
								entityNbt.put("SelectedItem", selectedItem.writeNbt(new NbtCompound()));
							}
						}
	
						return NbtUtils.matches(nbt, entityNbt, true) != inverted;
					});
				},
				parser -> true,
				TextComponents.translatable("argument.entity.options.nbt.description"));
			register(
				"scores",
				parser -> {
					StringReader reader = parser.getReader();

					reader.expect('{');
					reader.skipWhitespace();
	
					Map<String, NumberRange.Ints> scores = new HashMap<>();

					while (reader.canRead() && reader.peek() != '}') {
						reader.skipWhitespace();

						String objectiveName;
						NumberRange.Ints score;

						objectiveName = reader.readUnquotedString();

						reader.skipWhitespace();
						reader.expect('=');
						reader.skipWhitespace();

						score = NumberRange.Ints.fromString(reader);
						scores.put(objectiveName, score);

						reader.skipWhitespace();

						if (reader.canRead() && reader.peek() == ',') {
							reader.skip();
						}
					}
	
					reader.expect('}');

					if (!scores.isEmpty()) {
						parser.addPredicate(entity -> {
							Scoreboard scoreboard = entity.world.getScoreboard();
							String name = entity instanceof PlayerEntity
								? entity.getName()
								: entity.getUuidString();
	
							for (Map.Entry<String, NumberRange.Ints> entry : scores.entrySet()) {
								String objectiveName = entry.getKey();
								NumberRange.Ints score = entry.getValue();

								ScoreboardObjective objective = scoreboard.getObjective(objectiveName);

								if (objective == null) {
									return false;
								}
	
								if (!scoreboard.hasScore(name, objective)) {
									return false;
								}
	
								int entityScore = scoreboard.getScore(name, objective).get();

								if (!score.matches(entityScore)) {
									return false;
								}
							}
	
							return true;
						});
					}
	
					parser.setHasScores(true);
				},
				parser -> !parser.hasScores(),
				TextComponents.translatable("argument.entity.options.scores.description"));
			if (PARSE_ADVANCMENTS) register(
				"advancements",
				parser -> {
					StringReader reader = parser.getReader();

					reader.expect('{');
					reader.skipWhitespace();

					Map<Identifier, Predicate<AdvancementProgress>> advancements = new HashMap<>();
	
					while (reader.canRead() && reader.peek() != '}') {
						reader.skipWhitespace();

						Identifier key = IdentifierParser.parseIdentifier(reader);

						reader.skipWhitespace();
						reader.expect('=');
						reader.skipWhitespace();

						if (reader.canRead() && reader.peek() == '{') {
							Map<String, Predicate<CriterionProgress>> criterions = new HashMap<>();

							reader.skipWhitespace();
							reader.expect('{');
							reader.skipWhitespace();
	
							while (reader.canRead() && reader.peek() != '}') {
								reader.skipWhitespace();

								String criterionName;
								boolean complete;

								criterionName = reader.readUnquotedString();

								reader.skipWhitespace();
								reader.expect('=');
								reader.skipWhitespace();

								complete = reader.readBoolean();
								criterions.put(criterionName, progress -> progress.isComplete() == complete);

								reader.skipWhitespace();

								if (reader.canRead() && reader.peek() == ',') {
									reader.skip();
								}
							}
	
							reader.skipWhitespace();
							reader.expect('}');
							reader.skipWhitespace();

							advancements.put(key, progress -> {
								for (Map.Entry<String, Predicate<CriterionProgress>> entry : criterions.entrySet()) {
									CriterionProgress criterion = progress.getCriterion(entry.getKey());

									if (criterion == null || !entry.getValue().test(criterion)) {
										return false;
									}
								}
	
								return true;
							});
						} else {
							boolean complete = reader.readBoolean();
							advancements.put(key, progress -> progress.isComplete() == complete);
						}
	
						reader.skipWhitespace();

						if (reader.canRead() && reader.peek() == ',') {
							reader.skip();
						}
					}
	
					reader.expect('}');

					if (!advancements.isEmpty()) {
						parser.addPredicate(entity -> {
							if (entity instanceof ServerPlayerEntity) {
								ServerPlayerEntity player = (ServerPlayerEntity) entity;
								PlayerAdvancements playerAdvancements = player.getAdvancements();
								AdvancementManager advancementManager = player.getServer().getAdvancements();
								
								for (Map.Entry<Identifier, Predicate<AdvancementProgress>> entry : advancements.entrySet()) {
									Advancement advancement = advancementManager.get(entry.getKey());

									if (advancement == null || !entry.getValue().test(playerAdvancements.getProgress(advancement))) {
										return false;
									}
								}
								
								return true;
							} else {
								return false;
							}
						});

						parser.setAllowNonPlayers(false);
					}
	
					parser.setHasAdvancements(true);
				},
				parser -> !parser.hasAdvancements(),
				TextComponents.translatable("argument.entity.options.advancements.description"));
		}
	}

	public static Modifier get(EntitySelectorParser parser, String name, int cursor) throws CommandSyntaxException {
		Option option = OPTIONS.get(name);

		if (option != null) {
			if (option.predicate.test(parser)) {
				return option.modifier;
			} else {
				throw NOT_APPLICABLE_EXCEPTION.createWithContext(parser.getReader(), name);
			}
		} else {
			parser.getReader().setCursor(cursor);
			throw UNKNOWN_EXCEPTION.createWithContext(parser.getReader(), name);
		}
	}

	public static void suggest(EntitySelectorParser parser, SuggestionsBuilder builder) {
		String string = builder.getRemaining().toLowerCase(Locale.ROOT);

		for (Map.Entry<String, Option> entry : OPTIONS.entrySet()) {
			if (entry.getValue().predicate.test(parser) && entry.getKey().toLowerCase(Locale.ROOT).startsWith(string)) {
				builder.suggest(entry.getKey() + '=', entry.getValue().description);
			}
		}
	}

	private static class Option {

		public final Modifier modifier;
		public final Predicate<EntitySelectorParser> predicate;
		public final TextComponent description;

		private Option(Modifier modifier, Predicate<EntitySelectorParser> predicate, TextComponent description) {
			this.modifier = modifier;
			this.predicate = predicate;
			this.description = description;
		}
	}

	public interface Modifier {

		void apply(EntitySelectorParser parser) throws CommandSyntaxException;

	}
}
