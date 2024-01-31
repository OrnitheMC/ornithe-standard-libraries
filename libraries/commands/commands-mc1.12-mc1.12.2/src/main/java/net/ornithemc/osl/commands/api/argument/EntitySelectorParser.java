package net.ornithemc.osl.commands.api.argument;

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

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntitySelectorParser {

	public static final SimpleCommandExceptionType INVALID_EXCEPTION = new SimpleCommandExceptionType((Message) new TranslatableText("argument.entity.invalid", new Object[0]));
	public static final DynamicCommandExceptionType UNKNOWN_SELECTOR_EXCEPTION = new DynamicCommandExceptionType(selector -> (Message) new TranslatableText("argument.entity.selector.unknown", selector));
	public static final SimpleCommandExceptionType SELECTOR_NOT_ALLOWED_EXCEPTION = new SimpleCommandExceptionType((Message) new TranslatableText("argument.entity.selector.not_allowed", new Object[0]));
	public static final SimpleCommandExceptionType SELECTOR_MISSING_EXCEPTION = new SimpleCommandExceptionType((Message) new TranslatableText("argument.entity.selector.missing", new Object[0]));
	public static final SimpleCommandExceptionType UNTERMINATED_OPTIONS_EXCEPTION = new SimpleCommandExceptionType((Message) new TranslatableText("argument.entity.options.unterminated", new Object[0]));
	public static final DynamicCommandExceptionType OPTION_WITHOUT_VALUE_EXCEPTION = new DynamicCommandExceptionType(option -> (Message) new TranslatableText("argument.entity.options.valueless", option));
	public static final BiConsumer<Vec3d, List<? extends Entity>> ORDER_ARBITRARY = (pos, entities) -> { };
	public static final BiConsumer<Vec3d, List<? extends Entity>> ORDER_NEAREST = (pos, entities) -> entities.sort((entity1, entity2) -> Double.compare(pos.squaredDistanceTo(entity1.x, entity1.y, entity1.z), pos.squaredDistanceTo(entity2.x, entity2.y, entity2.z)));
	public static final BiConsumer<Vec3d, List<? extends Entity>> ORDER_FURTHEST = (pos, entities) -> entities.sort((entity1, entity2) -> Double.compare(pos.squaredDistanceTo(entity2.x, entity2.y, entity2.z), pos.squaredDistanceTo(entity1.x, entity1.y, entity1.z)));
	public static final BiConsumer<Vec3d, List<? extends Entity>> ORDER_RANDOM = (pos, entities) -> Collections.shuffle(entities);
	public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> NO_SUGGESTIONS = (builder, nameSuggestions) -> builder.buildFuture();

	private final StringReader reader;
	private final boolean allowSelectors;
	private int max;
	private boolean allowNonPlayers;
	private boolean restrictOtherWorlds;
	private Float minDistance;
	private Float maxDistance;
	private Integer minXp;
	private Integer maxXp;
	private Double x;
	private Double y;
	private Double z;
	private Double dx;
	private Double dy;
	private Double dz;
	private Integer minPitch;
	private Integer maxPitch;
	private Integer minYaw;
	private Integer maxYaw;
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
		Box box;
		if (this.dx != null || this.dy != null || this.dz != null) {
			box = this.getBounds(this.dx == null ? 0.0 : this.dx, this.dy == null ? 0.0 : this.dy,
					this.dz == null ? 0.0 : this.dz);
		} else if (this.maxDistance != null) {
			float f = this.maxDistance.floatValue();
			box = new Box(-f, -f, -f, f + 1.0f, f + 1.0f, f + 1.0f);
		} else {
			box = null;
		}
		Function<Vec3d, Vec3d> function = this.x == null && this.y == null && this.z == null ? pos -> pos
				: pos -> new Vec3d(this.x == null ? pos.x : this.x, this.y == null ? pos.y : this.y,
						this.z == null ? pos.z : this.z);
		return new EntitySelector(this.max, this.allowNonPlayers, this.restrictOtherWorlds, this.predicate,
				this.minDistance, this.maxDistance, function, box, this.order, this.allowSelf, this.name, this.uuid,
				this.type == null ? Entity.class : this.type, this.selector);
	}

	private Box getBounds(double dx, double dy, double dz) {
		boolean bl = dx < 0.0;
		boolean bl2 = dy < 0.0;
		boolean bl3 = dz < 0.0;
		double d = bl ? dx : 0.0;
		double e = bl2 ? dy : 0.0;
		double f = bl3 ? dz : 0.0;
		double g = (bl ? 0.0 : dx) + 1.0;
		double h = (bl2 ? 0.0 : dy) + 1.0;
		double i = (bl3 ? 0.0 : dz) + 1.0;
		return new Box(d, e, f, g, h, i);
	}

	private void buildPredicate() {
		if (this.minPitch != null || this.maxPitch != null) {
			this.predicate = this.predicate.and(this.buildRotationPredicate(this.minPitch, this.maxPitch, entity -> entity.pitch));
		}
		if (this.minYaw != null || this.maxYaw != null) {
			this.predicate = this.predicate.and(this.buildRotationPredicate(this.minYaw, this.maxYaw, entity -> entity.yaw));
		}
		if (this.minXp != null || this.maxXp != null) {
			this.predicate = this.predicate.and(entity -> {
				if (entity instanceof PlayerEntity) {
					int xp = ((PlayerEntity)entity).xpLevel;
					return (this.minXp == null || xp >= this.minXp) && (this.maxXp == null || xp <= this.maxXp);
				}
				return false;
			});
		}
	}

	private Predicate<Entity> buildRotationPredicate(Integer min, Integer max, ToDoubleFunction<Entity> getter) {
		double d = MathHelper.wrapDegrees(min == null ? 0.0F : min.floatValue());
		double e = MathHelper.wrapDegrees(max == null ? 359.0F : max.floatValue());
		return entity -> {
			double f = MathHelper.wrapDegrees(getter.applyAsDouble(entity));
			if (d > e) {
				return f >= d || f <= e;
			}
			return f >= d && f <= e;
		};
	}

	protected void parseSelector() throws CommandSyntaxException {
		this.selector = true;
		this.suggestions = this::suggestSelector;
		if (!this.reader.canRead()) {
			throw SELECTOR_MISSING_EXCEPTION.createWithContext(this.reader);
		}
		int i = this.reader.getCursor();
		char c = this.reader.read();
		if (c == 'p') {
			this.max = 1;
			this.allowNonPlayers = false;
			this.order = ORDER_NEAREST;
			this.setType(ServerPlayerEntity.class);
		} else if (c == 'a') {
			this.max = Integer.MAX_VALUE;
			this.allowNonPlayers = false;
			this.order = ORDER_ARBITRARY;
			this.setType(ServerPlayerEntity.class);
		} else if (c == 'r') {
			this.max = 1;
			this.allowNonPlayers = false;
			this.order = ORDER_RANDOM;
			this.setType(ServerPlayerEntity.class);
		} else if (c == 's') {
			this.max = 1;
			this.allowNonPlayers = true;
			this.allowSelf = true;
		} else if (c == 'e') {
			this.max = Integer.MAX_VALUE;
			this.allowNonPlayers = true;
			this.order = ORDER_ARBITRARY;
			this.predicate = Entity::isAlive;
		} else {
			this.reader.setCursor(i);
			throw UNKNOWN_SELECTOR_EXCEPTION.createWithContext(this.reader, '@' + String.valueOf(c));
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
		int i = this.reader.getCursor();
		String string = this.reader.readString();
		try {
			this.uuid = UUID.fromString(string);
			this.allowNonPlayers = true;
		} catch (IllegalArgumentException illegalArgumentException) {
			if (string.isEmpty() || string.length() > 16) {
				this.reader.setCursor(i);
				throw INVALID_EXCEPTION.createWithContext(this.reader);
			}
			this.allowNonPlayers = false;
			this.name = string;
		}
		this.max = 1;
	}

	protected void parseOptions() throws CommandSyntaxException {
		this.suggestions = this::suggestOptions;
		this.reader.skipWhitespace();
		while (this.reader.canRead() && this.reader.peek() != ']') {
			this.reader.skipWhitespace();
			int i = this.reader.getCursor();
			String string = this.reader.readString();
			EntitySelectorOptions.Modifier modifier = EntitySelectorOptions.get(this, string, i);
			this.reader.skipWhitespace();
			if (!this.reader.canRead() || this.reader.peek() != '=') {
				this.reader.setCursor(i);
				throw OPTION_WITHOUT_VALUE_EXCEPTION.createWithContext(this.reader, string);
			}
			this.reader.skip();
			this.reader.skipWhitespace();
			this.suggestions = NO_SUGGESTIONS;
			modifier.handle(this);
			this.reader.skipWhitespace();
			this.suggestions = this::suggestNextOptionOrEnd;
			if (!this.reader.canRead())
				continue;
			if (this.reader.peek() == ',') {
				this.reader.skip();
				this.suggestions = this::suggestOptions;
				continue;
			}
			if (this.reader.peek() == ']')
				break;
			throw UNTERMINATED_OPTIONS_EXCEPTION.createWithContext(this.reader);
		}
		if (!this.reader.canRead()) {
			throw UNTERMINATED_OPTIONS_EXCEPTION.createWithContext(this.reader);
		}
		this.reader.skip();
		this.suggestions = NO_SUGGESTIONS;
	}

	public boolean shouldInvertValue() {
		this.reader.skipWhitespace();
		if (this.reader.canRead() && this.reader.peek() == '!') {
			this.reader.skip();
			this.reader.skipWhitespace();
			return true;
		}
		return false;
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

	public Float getMinDistance() {
		return this.minDistance;
	}

	public void setMinDistance(Float distance) {
		this.minDistance = distance;
	}

	public Float getMaxDistance() {
		return this.maxDistance;
	}

	public void setMaxDistance(Float distance) {
		this.maxDistance = distance;
	}

	public Integer getMinXp() {
		return this.minXp;
	}

	public void setMinXp(Integer xp) {
		this.minXp = xp;
	}

	public Integer getMaxXp() {
		return this.maxXp;
	}

	public void setMaxXp(Integer xp) {
		this.maxXp = xp;
	}

	public Integer getMinPitch() {
		return this.minPitch;
	}

	public void setMinPitch(Integer pitch) {
		this.minPitch = pitch;
	}

	public Integer getMaxPitch() {
		return this.maxPitch;
	}

	public void setMaxPitch(Integer pitch) {
		this.maxPitch = pitch;
	}

	public Integer getMinYaw() {
		return this.minYaw;
	}

	public void setMinYaw(Integer yaw) {
		this.minYaw = yaw;
	}

	public Integer getMaxYaw() {
		return this.maxYaw;
	}

	public void setMaxYaw(Integer yaw) {
		this.maxYaw = yaw;
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
		builder.suggest("@p", (Message) new TranslatableText("argument.entity.selector.nearestPlayer", new Object[0]));
		builder.suggest("@a", (Message) new TranslatableText("argument.entity.selector.allPlayers", new Object[0]));
		builder.suggest("@r", (Message) new TranslatableText("argument.entity.selector.randomPlayer", new Object[0]));
		builder.suggest("@s", (Message) new TranslatableText("argument.entity.selector.self", new Object[0]));
		builder.suggest("@e", (Message) new TranslatableText("argument.entity.selector.allEntities", new Object[0]));
	}

	private CompletableFuture<Suggestions> suggestSelectorOrName(SuggestionsBuilder builder,
			Consumer<SuggestionsBuilder> nameSuggestions) {
		nameSuggestions.accept(builder);
		if (this.allowSelectors) {
			EntitySelectorParser.suggestSelectors(builder);
		}
		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestName(SuggestionsBuilder builder,
			Consumer<SuggestionsBuilder> nameSuggestions) {
		SuggestionsBuilder suggestionsBuilder = builder.createOffset(this.cursorStart);
		nameSuggestions.accept(suggestionsBuilder);
		return builder.add(suggestionsBuilder).buildFuture();
	}

	private CompletableFuture<Suggestions> suggestSelector(SuggestionsBuilder builder,
			Consumer<SuggestionsBuilder> nameSuggestions) {
		SuggestionsBuilder suggestionsBuilder = builder.createOffset(builder.getStart() - 1);
		EntitySelectorParser.suggestSelectors(suggestionsBuilder);
		builder.add(suggestionsBuilder);
		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestOptionsStart(SuggestionsBuilder builder,
			Consumer<SuggestionsBuilder> nameSuggestions) {
		builder.suggest(String.valueOf('['));
		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestOptionsOrEnd(SuggestionsBuilder builder,
			Consumer<SuggestionsBuilder> nameSuggestions) {
		builder.suggest(String.valueOf(']'));
		EntitySelectorOptions.suggest(this, builder);
		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestOptions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> nameSuggestions) {
		EntitySelectorOptions.suggest(this, builder);
		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestNextOptionOrEnd(SuggestionsBuilder builder,
			Consumer<SuggestionsBuilder> nameSuggestions) {
		builder.suggest(String.valueOf(','));
		builder.suggest(String.valueOf(']'));
		return builder.buildFuture();
	}

	public boolean hasSelf() {
		return this.allowSelf;
	}

	public void setSuggestions(
			BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions) {
		this.suggestions = suggestions;
	}

	public CompletableFuture<Suggestions> addSuggestions(SuggestionsBuilder builder,
			Consumer<SuggestionsBuilder> nameSuggestions) {
		return this.suggestions.apply(builder.createOffset(this.reader.getCursor()), nameSuggestions);
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
