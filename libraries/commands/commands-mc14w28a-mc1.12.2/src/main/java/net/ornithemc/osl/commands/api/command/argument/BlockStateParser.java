package net.ornithemc.osl.commands.api.command.argument;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.google.common.base.Optional;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.StateDefinition;
import net.minecraft.block.state.property.Property;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.text.api.TextComponents;

public class BlockStateParser {

	public static final SimpleCommandExceptionType NO_TAGS_ALLOWED_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.block.tag.disallowed"));
	public static final DynamicCommandExceptionType INVALID_ID_EXCEPTION = new DynamicCommandExceptionType(id -> TextComponents.translatable("argument.block.id.invalid", id));
	public static final Dynamic2CommandExceptionType INVALID_DATA_EXCEPTION = new Dynamic2CommandExceptionType((id, metadata) -> TextComponents.translatable("argument.block.data.invalid", id, metadata));
	public static final Dynamic2CommandExceptionType UNKNOWN_PROPERTY_EXCEPTION = new Dynamic2CommandExceptionType((id, property) -> TextComponents.translatable("argument.block.property.unknown", id, property));
	public static final Dynamic2CommandExceptionType DUPLICATE_PROPERTY_EXCEPTION = new Dynamic2CommandExceptionType((id, property) -> TextComponents.translatable("argument.block.property.duplicate", property, id));
	public static final Dynamic3CommandExceptionType INVALID_PROPERTY_EXCEPTION = new Dynamic3CommandExceptionType((id, property, value) -> TextComponents.translatable("argument.block.property.invalid", id, value, property));
	public static final Dynamic2CommandExceptionType NO_VALUE_EXCEPTION = new Dynamic2CommandExceptionType((id, property) -> TextComponents.translatable("argument.block.property.novalue", id, property));
	public static final SimpleCommandExceptionType UNCLOSED_PROPERTY_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.block.property.unclosed"));

	private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> NO_SUGGESTIONS = SuggestionsBuilder::buildFuture;

	private final StringReader reader;
	private final boolean legacyFormat;
	private final boolean allowTag;
	private final Map<Property<?>, Comparable<?>> properties = new HashMap<>();
	private final Map<String, String> vagueProperties = new HashMap<>();

	private NamespacedIdentifier id = new Identifier("");
	private StateDefinition definition;
	private BlockState state;
	private NbtCompound nbt;
	private NamespacedIdentifier tag = new Identifier("");
//	private int cursor;
	private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions = NO_SUGGESTIONS;

	public BlockStateParser(StringReader reader, boolean legacyFormat, boolean allowTag) {
		this.reader = reader;
		this.legacyFormat = legacyFormat;
//		this.allowTag = allowTag;
		this.allowTag = false; // TODO
	}

	public NamespacedIdentifier getId() {
		return this.id;
	}

	public Map<Property<?>, Comparable<?>> getProperties() {
		return this.properties;
	}

	public BlockState getState() {
		return this.state;
	}

	public NbtCompound getNbt() {
		return this.nbt;
	}

	public NamespacedIdentifier getTag() {
		return this.tag;
	}

	public BlockStateParser parse() throws CommandSyntaxException {
		this.suggestions = this::suggestBlockIdOrTag;

		if (!this.legacyFormat && this.reader.canRead() && this.reader.peek() == '#') {
			this.parseTag();
			this.suggestions = this::suggestStartVaguePropertyOrNbt;

			if (this.reader.canRead() && this.reader.peek() == '[') {
				this.parseVagueProperties();
				this.suggestions = this::suggestNbtStart;
			}
		} else {
			this.parseBlock();
			if (!this.legacyFormat) {
				this.suggestions = this::suggestStartPropertiesOrNbt;
			}

			if (this.reader.canRead() && this.reader.peek() == (this.legacyFormat ? ' ' : '[')) {
				this.parseProperties();
				if (!this.legacyFormat) {
					this.suggestions = this::suggestNbtStart;
				}
			}
		}

		if (this.reader.canRead() && this.reader.peek() == '{') {
			this.suggestions = NO_SUGGESTIONS;
			if (!this.legacyFormat) {
				this.parseNbt();
			}
		}

		return this;
	}

	private CompletableFuture<Suggestions> suggestPropertyOrEnd(SuggestionsBuilder builder) {
		if (builder.getRemaining().isEmpty()) {
			builder.suggest(String.valueOf(']'));
		}

		return this.suggestProperty(builder);
	}

	private CompletableFuture<Suggestions> suggestVaguePropertyOrEnd(SuggestionsBuilder builder) {
		if (builder.getRemaining().isEmpty()) {
			builder.suggest(String.valueOf(']'));
		}

		return this.suggestVagueProperty(builder);
	}

	private CompletableFuture<Suggestions> suggestProperty(SuggestionsBuilder builder) {
		String s = builder.getRemaining().toLowerCase(Locale.ROOT);

		for (Property<?> property : this.state.properties()) {
			if (!this.properties.containsKey(property) && property.getName().startsWith(s)) {
				builder.suggest(property.getName() + '=');
			}
		}

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestVagueProperty(SuggestionsBuilder builder) {
/*		String s = builder.getRemaining().toLowerCase(Locale.ROOT);

		if (this.tag != null && !this.tag.identifier().isEmpty()) {
			Tag<Block> tag = BlockTags.getAll().getTag(this.tag);

			if (tag != null) {
				for (Block block : tag.values()) {
					for (Property<?> property : block.stateDefinition().properties()) {
						if (!this.vagueProperties.containsKey(property.getName()) && property.getName().startsWith(s)) {
							builder.suggest(property.getName() + '=');
						}
					}
				}
			}
		}*/

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestNbtStart(SuggestionsBuilder builder) {
		if (builder.getRemaining().isEmpty() && this.state.getBlock().hasBlockEntity()) {
			builder.suggest(String.valueOf('{'));
		}

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestPropertyValueSeparator(SuggestionsBuilder builder) {
		if (builder.getRemaining().isEmpty()) {
			builder.suggest(String.valueOf('='));
		}

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestNextPropertyOrEnd(SuggestionsBuilder builder) {
		if (builder.getRemaining().isEmpty()) {
			builder.suggest(String.valueOf(']'));
		}

		return this.suggestNextProperty(builder);
	}

	private CompletableFuture<Suggestions> suggestNextProperty(SuggestionsBuilder builder) {
		if (builder.getRemaining().isEmpty() && this.properties.size() < this.state.properties().size()) {
			builder.suggest(String.valueOf(','));
		}

		return builder.buildFuture();
	}

	private static <T extends Comparable<T>> SuggestionsBuilder suggestValues(SuggestionsBuilder builder, Property<T> property) {
		for (T value : property.values()) {
			if (value instanceof Integer) {
				builder.suggest((Integer) value);
			} else {
				builder.suggest(property.getName(value));
			}
		}

		return builder;
	}

	private CompletableFuture<Suggestions> suggestVagueValue(SuggestionsBuilder builder, String propertyName) {
/*		boolean hasOtherProperties = false;

		if (this.tag != null && !this.tag.identifier().isEmpty()) {
			Tag<Block> tag = BlockTags.getAll().getTag(this.tag);

			if (tag != null) {
				for (Block block : tag.values()) {
					Property<?> property = block.stateDefinition().getProperty(propertyName);

					if (property != null) {
						suggestValues(builder, property);
					}

					if (!hasOtherProperties) {
						for (Property<?> otherProperty : block.stateDefinition().properties()) {
							if (!this.vagueProperties.containsKey(otherProperty.getName())) {
								hasOtherProperties = true;
								break;
							}
						}
					}
				}
			}
		}

		if (hasOtherProperties) {
			builder.suggest(String.valueOf(','));
		}

		builder.suggest(String.valueOf(']'));*/

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestStartVaguePropertyOrNbt(SuggestionsBuilder builder) {
/*		if (builder.getRemaining().isEmpty()) {
			Tag<Block> tag = BlockTags.getAll().getTag(this.tag);

			if (tag != null) {
				boolean hasProperty = false;
				boolean hasBlockEntity = false;

				for (Block block : tag.values()) {
					hasProperty |= !block.stateDefinition().properties().isEmpty();
					hasBlockEntity |= block.hasBlockEntity();

					if (hasProperty && hasBlockEntity) {
						break;
					}
				}

				if (hasProperty) {
					builder.suggest(String.valueOf('['));
				}

				if (hasBlockEntity) {
					builder.suggest(String.valueOf('{'));
				}
			}
		}*/

		return this.suggestTag(builder);
	}

	private CompletableFuture<Suggestions> suggestStartPropertiesOrNbt(SuggestionsBuilder builder) {
		if (builder.getRemaining().isEmpty()) {
			if (!this.state.getBlock().stateDefinition().properties().isEmpty()) {
				builder.suggest(String.valueOf('['));
			}
			if (this.state.getBlock().hasBlockEntity()) {
				builder.suggest(String.valueOf('{'));
			}
		}

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestTag(SuggestionsBuilder builder) {
		return builder.buildFuture();
 //		return SuggestionProvider.suggestResource(BlockTags.getAll().getKeys(), builder.createOffset(this.cursor).add(builder));
	}

	private CompletableFuture<Suggestions> suggestBlockIdOrTag(SuggestionsBuilder builder) {
		if (this.allowTag) {
//			SuggestionProvider.suggestResource(BlockTags.getAll().getKeys(), builder, String.valueOf('#'));
		}

		return SharedSuggestionProvider.suggestResource(BlockRegistry.identifierSet(), builder);
	}

	private boolean isValidPartialPropertyName(String name) {
		for (Property<?> property : this.state.properties()) {
			if (!this.properties.containsKey(property) && property.getName().startsWith(name)) {
				return true;
			}
		}

		return false;
	}

	public void parseBlock() throws CommandSyntaxException {
		int resetCursor = this.reader.getCursor();

		this.id = IdentifierParser.parse(this.reader);
		Block block = BlockRegistry.getBlock(this.id);

		if (!BlockRegistry.identifierSet().contains(this.id)) {
			this.reader.setCursor(resetCursor);
			throw INVALID_ID_EXCEPTION.createWithContext(this.reader, this.id.toString());
		}

		this.definition = block.stateDefinition();
		this.state = block.defaultState();
	}

	public void parseTag() throws CommandSyntaxException {
		if (!this.allowTag) {
			throw NO_TAGS_ALLOWED_EXCEPTION.create();
		}

		this.suggestions = this::suggestTag;
		this.reader.expect('#');
//		this.cursor = this.reader.getCursor();
		this.tag = IdentifierParser.parseIdentifier(this.reader);
	}

	public void parseProperties() throws CommandSyntaxException {
		int resetCursor = this.reader.getCursor();

		if (this.legacyFormat) {
			this.reader.expect(' ');
			this.suggestions = this::suggestProperty;
		} else {
			this.reader.skip();
			this.suggestions = this::suggestPropertyOrEnd;
			this.reader.skipWhitespace();
		}

		while (this.reader.canRead() && this.reader.peek() != (this.legacyFormat ? ' ' : ']')) {
			if (this.legacyFormat) {
				if (Character.isDigit(this.reader.peek())) {
					this.suggestions = NO_SUGGESTIONS;

					resetCursor = this.reader.getCursor();
					int metadata = this.reader.readInt();

					if (metadata < 0 || metadata > 15) {
						this.reader.setCursor(resetCursor);
						throw INVALID_DATA_EXCEPTION.createWithContext(this.reader, this.id, metadata);
					}

					this.state = this.definition.getBlock().getStateFromMetadata(metadata);

					for (Property<?> property : this.definition.properties()) {
						this.properties.put(property, this.state.get(property));
					}

					break;
				}
			} else {
				this.reader.skipWhitespace();
				resetCursor = this.reader.getCursor();
			}

			int propertyStartCursor = this.reader.getCursor();

			String propertyName = this.reader.readString();
			Property<?> property = this.definition.getProperty(propertyName);

			if (property == null) {
				if (this.legacyFormat && !this.isValidPartialPropertyName(propertyName)) {
					this.suggestions = NO_SUGGESTIONS;
					this.reader.setCursor(resetCursor);

					break; // allow block only!
				} else {
					this.reader.setCursor(propertyStartCursor);
					throw UNKNOWN_PROPERTY_EXCEPTION.createWithContext(this.reader, this.id.toString(), propertyName);
				}
			}
			if (this.properties.containsKey(property)) {
				this.reader.setCursor(propertyStartCursor);
				throw DUPLICATE_PROPERTY_EXCEPTION.createWithContext(this.reader, this.id.toString(), propertyName);
			}

			if (!this.legacyFormat) {
				this.reader.skipWhitespace();
			}
			this.suggestions = this::suggestPropertyValueSeparator;

			if (!this.reader.canRead() || this.reader.peek() != '=') {
				throw NO_VALUE_EXCEPTION.createWithContext(this.reader, this.id.toString(), propertyName);
			}

			this.reader.skip();
			if (!this.legacyFormat) {
				this.reader.skipWhitespace();
			}
			this.suggestions = builder -> suggestValues(builder, property).buildFuture();

			int valueStartCursor = this.reader.getCursor();
			this.setValue(property, this.reader.readString(), valueStartCursor);

			if (this.legacyFormat) {
				this.suggestions = this::suggestNextProperty;
			} else {
				this.suggestions = this::suggestNextPropertyOrEnd;
				this.reader.skipWhitespace();
			}

			if (this.reader.canRead()) {
				if (this.reader.peek() == ',') {
					this.reader.skip();
					this.suggestions = this::suggestProperty;
				} else if (this.legacyFormat || this.reader.peek() == ']') {
					break;
				} else {
					throw UNCLOSED_PROPERTY_EXCEPTION.createWithContext(this.reader);
				}
			}
		}

		if (!this.legacyFormat) {
			if (this.reader.canRead()) {
				this.reader.skip();
			} else {
				throw UNCLOSED_PROPERTY_EXCEPTION.createWithContext(this.reader);
			}
		}
	}

	public void parseVagueProperties() throws CommandSyntaxException {
		this.reader.skip();
		this.reader.skipWhitespace();
		this.suggestions = this::suggestVaguePropertyOrEnd;

		int resetCursor = -1;

		while (this.reader.canRead() && this.reader.peek() != ']') {
			this.reader.skipWhitespace();

			int propertyStartCursor = this.reader.getCursor();
			String propertyName = this.reader.readString();

			if (this.vagueProperties.containsKey(propertyName)) {
				this.reader.setCursor(propertyStartCursor);
				throw DUPLICATE_PROPERTY_EXCEPTION.createWithContext(this.reader, this.id.toString(), propertyName);
			}

			this.reader.skipWhitespace();

			if (!this.reader.canRead() || this.reader.peek() != '=') {
				this.reader.setCursor(propertyStartCursor);
				throw NO_VALUE_EXCEPTION.createWithContext(this.reader, this.id.toString(), propertyName);
			}

			this.reader.skip();
			this.reader.skipWhitespace();
			this.suggestions = builder -> this.suggestVagueValue(builder, propertyName);

			resetCursor = this.reader.getCursor();
			String value = this.reader.readString();

			this.vagueProperties.put(propertyName, value);
			this.reader.skipWhitespace();

			if (this.reader.canRead()) {
				resetCursor = -1;

				if (this.reader.peek() == ',') {
					this.reader.skip();
					this.suggestions = this::suggestVagueProperty;
				} else if (this.reader.peek() == ']') {
					break;
				} else {
					throw UNCLOSED_PROPERTY_EXCEPTION.createWithContext(this.reader);
				}
			}
		}

		if (this.reader.canRead()) {
			this.reader.skip();
		} else {
			if (resetCursor >= 0) {
				this.reader.setCursor(resetCursor);
			}

			throw UNCLOSED_PROPERTY_EXCEPTION.createWithContext(this.reader);
		}
	}

	public void parseNbt() throws CommandSyntaxException {
		this.nbt = new SnbtParser(this.reader).readCompound();
	}

	private <T extends Comparable<T>> void setValue(Property<T> property, String valueName, int cursor) throws CommandSyntaxException {
		Optional<T> value = property.getValue(valueName);

		if (!value.isPresent()) {
			this.reader.setCursor(cursor);
			throw INVALID_PROPERTY_EXCEPTION.createWithContext(this.reader, this.id.toString(), property.getName(), valueName);
		}

		this.state = this.state.set(property, value.get());
		this.properties.put(property, value.get());
	}

	public static String serializeProperties(BlockState state) {
		StringBuilder sb = new StringBuilder(BlockRegistry.getIdentifier(state.getBlock()).toString());

		if (!state.properties().isEmpty()) {
			boolean appendSeparator = false;

			for (Map.Entry<Property<?>, Comparable<?>> entry : state.values().entrySet()) {
				if (appendSeparator) {
					sb.append(',');
				} else {
					appendSeparator = true;
				}

				serializeProperty(sb, entry.getKey(), entry.getValue());
			}
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> void serializeProperty(StringBuilder sb, Property<T> property, Comparable<?> value) {
		sb.append(property.getName());
		sb.append('=');
		sb.append(property.getName((T) value));
	}

	public CompletableFuture<Suggestions> addSuggestions(SuggestionsBuilder builder) {
		return this.suggestions.apply(builder.createOffset(this.reader.getCursor()));
	}
}
