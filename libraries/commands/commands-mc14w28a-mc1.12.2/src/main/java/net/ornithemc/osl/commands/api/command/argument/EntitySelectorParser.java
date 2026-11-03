package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.text.api.TextComponents;

public class EntitySelectorParser {

	public static final SimpleCommandExceptionType INVALID_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.invalid"));
	public static final DynamicCommandExceptionType UNKNOWN_SELECTOR_EXCEPTION = new DynamicCommandExceptionType(selector -> TextComponents.translatable("argument.entity.selector.unknown", selector));
	public static final SimpleCommandExceptionType SELECTOR_NOT_ALLOWED_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.selector.not_allowed"));
	public static final SimpleCommandExceptionType SELECTOR_MISSING_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.selector.missing"));
	public static final SimpleCommandExceptionType UNTERMINATED_OPTIONS_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.entity.options.unterminated"));
	public static final DynamicCommandExceptionType OPTION_WITHOUT_VALUE_EXCEPTION = new DynamicCommandExceptionType(option -> TextComponents.translatable("argument.entity.options.valueless", option));
	public static final BiConsumer<Vec3d, List<? extends Entity>> ORDER_ARBITRARY = (pos, entities) -> { };
	public static final BiConsumer<Vec3d, List<? extends Entity>> ORDER_NEAREST = (pos, entities) -> entities.sort((entity1, entity2) -> Double.compare(entity1.squaredDistanceTo(pos.x, pos.y, pos.z), entity2.squaredDistanceTo(pos.x, pos.y, pos.z)));
	public static final BiConsumer<Vec3d, List<? extends Entity>> ORDER_FURTHEST = (pos, entities) -> entities.sort((entity1, entity2) -> Double.compare(entity2.squaredDistanceTo(pos.x, pos.y, pos.z), entity1.squaredDistanceTo(pos.x, pos.y, pos.z)));
	public static final BiConsumer<Vec3d, List<? extends Entity>> ORDER_RANDOM = (pos, entities) -> Collections.shuffle(entities);
	public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> NO_SUGGESTIONS = (builder, names) -> builder.buildFuture();
	public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> SORT_ORDER_SUGGESTIONS = (builder, names) -> SharedSuggestionProvider.suggestMatching(Arrays.asList("nearest", "furthest", "random", "arbitrary"), builder);

	private final StringReader reader;
	private final boolean allowSelectors;
	private int max;
	private boolean allowNonPlayers;
	private boolean restrictOtherWorlds;
	private NumberRange.Floats distance = NumberRange.Floats.ANY;
	private NumberRange.Ints xp = NumberRange.Ints.ANY;
	private Double x;
	private Double y;
	private Double z;
	private Double dx;
	private Double dy;
	private Double dz;
	private WrappedNumberRange pitch = WrappedNumberRange.ANY;
	private WrappedNumberRange yaw = WrappedNumberRange.ANY;
	private Predicate<Entity> predicate = entity -> true;
	private BiConsumer<Vec3d, List<? extends Entity>> order = ORDER_ARBITRARY;
	private boolean allowSelf;
	private String name;
	private int cursorStart;
	private UUID uuid;
	private BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions = NO_SUGGESTIONS;
	private boolean hasName;
	private boolean hasNotName;
	private boolean hasLimit;
	private boolean hasOrder;
	private boolean hasGameMode;
	private boolean hasNotGameMode;
	private boolean hasTeam;
	private boolean hasNotTeam;
	private Class<? extends Entity> type;
	private boolean hasNotType;
	private boolean hasScores;
	private boolean hasAdvancements;
	private boolean selector;

	public EntitySelectorParser(StringReader reader) {
		this(reader, true);
	}

	public EntitySelectorParser(StringReader reader, boolean allowSelectors) {
		this.reader = reader;
		this.allowSelectors = allowSelectors;
	}

	public EntitySelector getSelector() {
		Box bounds;
		Function<Vec3d, Vec3d> position;

		if (this.dx == null && this.dy == null && this.dz == null) {
			if (this.distance.getMax() != null) {
				float max = this.distance.getMax();

				bounds = new Box(-max, -max, -max, max + 1.0F, max + 1.0F, max + 1.0F);
			} else {
				bounds = null;
			}
		} else {
			bounds = this.getBounds(
				this.dx == null ? 0.0 : this.dx,
				this.dy == null ? 0.0 : this.dy,
				this.dz == null ? 0.0 : this.dz
			);
		}

		if (this.x == null && this.y == null && this.z == null) {
			position = pos -> pos;
		} else {
			position = pos -> new Vec3d(
				this.x == null ? pos.x : this.x,
				this.y == null ? pos.y : this.y,
				this.z == null ? pos.z : this.z
			);
		}

		return new EntitySelector(this.max, this.allowNonPlayers, this.restrictOtherWorlds, this.predicate, this.distance, position, bounds, this.order, this.allowSelf, this.name, this.uuid, this.type == null ? Entity.class : this.type, this.selector);
	}

	private Box getBounds(double dx, double dy, double dz) {
		boolean negativeX = dx < 0.0;
		boolean negativeY = dy < 0.0;
		boolean negativeZ = dz < 0.0;

		double minX = negativeX ? dx : 0.0;
		double minY = negativeY ? dy : 0.0;
		double minZ = negativeZ ? dz : 0.0;
		double maxX = (negativeX ? 0.0 : dx) + 1.0;
		double maxY = (negativeY ? 0.0 : dy) + 1.0;
		double maxZ = (negativeZ ? 0.0 : dz) + 1.0;

		return new Box(minX, minY, minZ, maxX, maxY, maxZ);
	}

	private void buildPredicate() {
		if (this.pitch != WrappedNumberRange.ANY) {
			this.predicate = this.predicate.and(this.buildRotationPredicate(this.pitch, entity -> entity.pitch));
		}

		if (this.yaw != WrappedNumberRange.ANY) {
			this.predicate = this.predicate.and(this.buildRotationPredicate(this.yaw, entity -> entity.yaw));
		}

		if (!this.xp.isAny()) {
			this.predicate = this.predicate.and(entity -> entity instanceof ServerPlayerEntity && this.xp.matches(((ServerPlayerEntity) entity).xpLevel));
		}
	}

	private Predicate<Entity> buildRotationPredicate(WrappedNumberRange value, ToDoubleFunction<Entity> getter) {
		double min = MathHelper.wrapDegrees(value.getMin() == null ? 0.0F : value.getMin());
		double max = MathHelper.wrapDegrees(value.getMax() == null ? 359.0F : value.getMax());

		return entity -> {
			double degrees = MathHelper.wrapDegrees(getter.applyAsDouble(entity));
			return min > max ? degrees >= min || degrees <= max : degrees >= min && degrees <= max;
		};
	}

	protected void parseSelector() throws CommandSyntaxException {
		this.selector = true;
		this.suggestions = this::suggestSelector;

		if (!this.reader.canRead()) {
			throw SELECTOR_MISSING_EXCEPTION.createWithContext(this.reader);
		}

		int startCursor = this.reader.getCursor();
		char selectorType = this.reader.read();

		if (selectorType == 'p') {
			this.max = 1;
			this.allowNonPlayers = false;
			this.order = ORDER_NEAREST;
			this.setType(ServerPlayerEntity.class);
		} else if (selectorType == 'a') {
			this.max = Integer.MAX_VALUE;
			this.allowNonPlayers = false;
			this.order = ORDER_ARBITRARY;
			this.setType(ServerPlayerEntity.class);
		} else if (selectorType == 'r') {
			this.max = 1;
			this.allowNonPlayers = false;
			this.order = ORDER_RANDOM;
			this.setType(ServerPlayerEntity.class);
		} else if (selectorType == 's') {
			this.max = 1;
			this.allowNonPlayers = true;
			this.allowSelf = true;
		} else if (selectorType != 'e') {
			this.max = Integer.MAX_VALUE;
			this.allowNonPlayers = true;
			this.order = ORDER_ARBITRARY;
			this.predicate = Entity::isAlive;
		} else {
			this.reader.setCursor(startCursor);
			throw UNKNOWN_SELECTOR_EXCEPTION.createWithContext(this.reader, '@' + String.valueOf(selectorType));
		}

		this.suggestions = this::suggestOptionsStart;

		if (this.reader.canRead() && this.reader.peek() == '[') {
			this.reader.skip();
			this.suggestions = this::suggestOptionsOrEnd;
			this.parseOptions();
		}
	}

	protected void parseNameOrUuid() throws CommandSyntaxException {
		if (this.reader.canRead()) {
			this.suggestions = this::suggestName;
		}

		int startCursor = this.reader.getCursor();
		String name = this.reader.readString();

		try {
			this.uuid = UUID.fromString(name);
			this.allowNonPlayers = true;
		} catch (IllegalArgumentException var4) {
			if (name.isEmpty() || name.length() > 16) {
				this.reader.setCursor(startCursor);
				throw INVALID_EXCEPTION.createWithContext(this.reader);
			}

			this.allowNonPlayers = false;
			this.name = name;
		}

		this.max = 1;
	}

	protected void parseOptions() throws CommandSyntaxException {
		this.suggestions = this::suggestOptions;
		this.reader.skipWhitespace();

		while (this.reader.canRead() && this.reader.peek() != ']') {
			this.reader.skipWhitespace();
			int startCursor = this.reader.getCursor();

			String option = this.reader.readString();
			EntitySelectorOptions.Modifier modifier = EntitySelectorOptions.get(this, option, startCursor);

			this.reader.skipWhitespace();

			if (!this.reader.canRead() || this.reader.peek() != '=') {
				this.reader.setCursor(startCursor);
				throw OPTION_WITHOUT_VALUE_EXCEPTION.createWithContext(this.reader, option);
			}

			this.reader.skip();
			this.reader.skipWhitespace();
			this.suggestions = NO_SUGGESTIONS;

			modifier.apply(this);

			this.reader.skipWhitespace();
			this.suggestions = this::suggestNextOptionOrEnd;

			if (this.reader.canRead()) {
				if (this.reader.peek() == ',') {
					this.reader.skip();
					this.suggestions = this::suggestOptions;
				} else if (this.reader.peek() == ']') {
					break;
				} else {
					throw UNTERMINATED_OPTIONS_EXCEPTION.createWithContext(this.reader);
				}
			}
		}

		if (this.reader.canRead()) {
			this.reader.skip();
			this.suggestions = NO_SUGGESTIONS;
		} else {
			throw UNTERMINATED_OPTIONS_EXCEPTION.createWithContext(this.reader);
		}
	}

	public boolean shouldInvertValue() {
		this.reader.skipWhitespace();

		if (this.reader.canRead() && this.reader.peek() == '!') {
			this.reader.skip();
			this.reader.skipWhitespace();

			return true;
		} else {
			return false;
		}
	}

	public StringReader getReader() {
		return this.reader;
	}

	public void addPredicate(Predicate<Entity> predicate) {
		this.predicate = this.predicate.and(predicate);
	}

	public void setRestrictOtherWorlds() {
		this.restrictOtherWorlds = true;
	}

	public NumberRange.Floats getDistance() {
		return this.distance;
	}

	public void setDistance(NumberRange.Floats distance) {
		this.distance = distance;
	}

	public NumberRange.Ints getXp() {
		return this.xp;
	}

	public void setXp(NumberRange.Ints xp) {
		this.xp = xp;
	}

	public WrappedNumberRange getPitch() {
		return this.pitch;
	}

	public void setPitch(WrappedNumberRange pitch) {
		this.pitch = pitch;
	}

	public WrappedNumberRange getYaw() {
		return this.yaw;
	}

	public void setYaw(WrappedNumberRange yaw) {
		this.yaw = yaw;
	}

	public Double getX() {
		return this.x;
	}

	public Double getY() {
		return this.y;
	}

	public Double getZ() {
		return this.z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public void setDz(double dz) {
		this.dz = dz;
	}

	public Double getDx() {
		return this.dx;
	}

	public Double getDy() {
		return this.dy;
	}

	public Double getDz() {
		return this.dz;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setAllowNonPlayers(boolean allowNonPlayers) {
		this.allowNonPlayers = allowNonPlayers;
	}

	public void setOrder(BiConsumer<Vec3d, List<? extends Entity>> order) {
		this.order = order;
	}

	public EntitySelector parse() throws CommandSyntaxException {
		this.cursorStart = this.reader.getCursor();
		this.suggestions = this::suggestSelectorOrName;

		if (this.reader.canRead() && this.reader.peek() == '@') {
			if (!this.allowSelectors) {
				throw SELECTOR_NOT_ALLOWED_EXCEPTION.createWithContext(this.reader);
			}

			this.reader.skip();
			this.parseSelector();
		} else {
			this.parseNameOrUuid();
		}

		this.buildPredicate();

		return this.getSelector();
	}

	private static void suggestSelectors(SuggestionsBuilder builder) {
		builder.suggest("@p", TextComponents.translatable("argument.entity.selector.nearestPlayer"));
		builder.suggest("@a", TextComponents.translatable("argument.entity.selector.allPlayers"));
		builder.suggest("@r", TextComponents.translatable("argument.entity.selector.randomPlayer"));
		builder.suggest("@s", TextComponents.translatable("argument.entity.selector.self"));
		builder.suggest("@e", TextComponents.translatable("argument.entity.selector.allEntities"));
	}

	private CompletableFuture<Suggestions> suggestSelectorOrName(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
		names.accept(builder);

		if (this.allowSelectors) {
			suggestSelectors(builder);
		}

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestName(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
		SuggestionsBuilder nameSuggestionBuilder = builder.createOffset(this.cursorStart);
		names.accept(nameSuggestionBuilder);

		return builder.add(nameSuggestionBuilder).buildFuture();
	}

	private CompletableFuture<Suggestions> suggestSelector(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
		SuggestionsBuilder selectorSuggestionBuilder = builder.createOffset(builder.getStart() - 1);

		suggestSelectors(selectorSuggestionBuilder);
		builder.add(selectorSuggestionBuilder);

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestOptionsStart(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
		builder.suggest(String.valueOf('['));
		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestOptionsOrEnd(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
		builder.suggest(String.valueOf(']'));
		EntitySelectorOptions.suggest(this, builder);

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestOptions(SuggestionsBuilder builder,Consumer<SuggestionsBuilder> names) {
		EntitySelectorOptions.suggest(this, builder);
		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestNextOptionOrEnd(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
		builder.suggest(String.valueOf(','));
		builder.suggest(String.valueOf(']'));

		return builder.buildFuture();
	}

	public boolean hasSelf() {
		return this.allowSelf;
	}

	public void setSuggestions(BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions) {
		this.suggestions = suggestions;
	}

	public CompletableFuture<Suggestions> addSuggestions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
		return this.suggestions.apply(builder.createOffset(this.reader.getCursor()), names);
	}

	public boolean hasName() {
		return this.hasName;
	}

	public void setHasName(boolean hasName) {
		this.hasName = hasName;
	}

	public boolean hasNotName() {
		return this.hasNotName;
	}

	public void setHasNotName(boolean hasNotName) {
		this.hasNotName = hasNotName;
	}

	public boolean hasLimit() {
		return this.hasLimit;
	}

	public void setHasLimit(boolean hasLimit) {
		this.hasLimit = hasLimit;
	}

	public boolean hasOrder() {
		return this.hasOrder;
	}

	public void setHasOrder(boolean hasOrder) {
		this.hasOrder = hasOrder;
	}

	public boolean hasGameMode() {
		return this.hasGameMode;
	}

	public void setHasGameMode(boolean hasGameMode) {
		this.hasGameMode = hasGameMode;
	}

	public boolean hasNotGameMode() {
		return this.hasNotGameMode;
	}

	public void setHasNotGameMode(boolean hasNotGameMode) {
		this.hasNotGameMode = hasNotGameMode;
	}

	public boolean hasTeam() {
		return this.hasTeam;
	}

	public void setHasTeam(boolean hasTeam) {
		this.hasTeam = hasTeam;
	}

	public void setHasNotTeam(boolean hasNotTeam) {
		this.hasNotTeam = hasNotTeam;
	}

	public boolean hasNotTeam() {
		return this.hasNotTeam;
	}

	public void setType(Class<? extends Entity> type) {
		this.type = type;
	}

	public void setHasNotType() {
		this.hasNotType = true;
	}

	public boolean hasType() {
		return this.type != null;
	}

	public boolean hasNotType() {
		return this.hasNotType;
	}

	public boolean hasScores() {
		return this.hasScores;
	}

	public void setHasScores(boolean hasScores) {
		this.hasScores = hasScores;
	}

	public boolean hasAdvancements() {
		return this.hasAdvancements;
	}

	public void setHasAdvancements(boolean hasAdvancements) {
		this.hasAdvancements = hasAdvancements;
	}
}
