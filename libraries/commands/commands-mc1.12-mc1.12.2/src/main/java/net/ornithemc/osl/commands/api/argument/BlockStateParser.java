package net.ornithemc.osl.commands.api.argument;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.base.Optional;

import com.mojang.brigadier.Message;
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
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.commands.api.SuggestionProvider;
import net.ornithemc.osl.resource.loader.api.ResourceUtils;

public class BlockStateParser {

	public static final DynamicCommandExceptionType INVALID_ID_EXCEPTION = new DynamicCommandExceptionType(id -> (Message)new TranslatableText("argument.block.id.invalid", id));
	public static final Dynamic2CommandExceptionType INVALID_DATA_EXCEPTION = new Dynamic2CommandExceptionType((id, metadata) -> (Message)new TranslatableText("argument.block.data.invalid", id, metadata));
	public static final Dynamic2CommandExceptionType UNKNOWN_PROPERTY_EXCEPTION = new Dynamic2CommandExceptionType((id, property) -> (Message)new TranslatableText("argument.block.property.unknown", id, property));
	public static final Dynamic2CommandExceptionType DUPLICATE_PROPERTY_EXCEPTION = new Dynamic2CommandExceptionType((id, property) -> (Message)new TranslatableText("argument.block.property.duplicate", property, id));
	public static final Dynamic3CommandExceptionType INVALID_PROPERTY_EXCEPTION = new Dynamic3CommandExceptionType((id, property, value) -> (Message)new TranslatableText("argument.block.property.invalid", id, value, property));
	public static final Dynamic2CommandExceptionType NO_VALUE_EXCEPTION = new Dynamic2CommandExceptionType((id, property) -> (Message)new TranslatableText("argument.block.property.novalue", id, property));
	public static final SimpleCommandExceptionType UNCLOSED_PROPERTY_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.block.property.unclosed", new Object[0]));

	private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> NO_SUGGESTIONS = SuggestionsBuilder::buildFuture;

	private final StringReader reader;
	private final boolean legacy;
	private final Map<Property<?>, Comparable<?>> properties = new HashMap<>();

	private Identifier id = new Identifier("");
	private StateDefinition definition;
	private BlockState state;
	private NbtCompound nbt;
	private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions = NO_SUGGESTIONS;

	public BlockStateParser(StringReader reader, boolean legacy) {
		this.reader = reader;
		this.legacy = legacy;
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

	public BlockStateParser parse() throws CommandSyntaxException {
		return parse(false);
	}

	public BlockStateParser parse(boolean parseNbt) throws CommandSyntaxException {
		if (!this.legacy) {
			this.suggestions = this::suggestBlockId;
		}
		this.parseBlock();
		if (legacy) {
			this.suggestions = this::suggestNextProperty;
			this.parseProperties();
			this.suggestions = NO_SUGGESTIONS;
		} else {
			this.suggestions = this::suggestStartPropertiesOrNbt;
			if (this.reader.canRead() && this.reader.peek() == '[') {
				this.parseProperties();
				this.suggestions = this::suggestNbtStart;
			}
			if (parseNbt && this.reader.canRead() && this.reader.peek() == '{') {
				this.suggestions = NO_SUGGESTIONS;
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

	private CompletableFuture<Suggestions> suggestProperty(SuggestionsBuilder builder) {
		String s = builder.getRemaining().toLowerCase(Locale.ROOT);
		for (Property<?> property : this.state.properties()) {
			if (!this.properties.containsKey(property) && property.getName().startsWith(s)) {
				builder.suggest(property.getName() + '=');
			}
		}
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
				builder.suggest((Integer)value);
				continue;
			}
			builder.suggest(property.getName(value));
		}
		return builder;
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

	private CompletableFuture<Suggestions> suggestBlockId(SuggestionsBuilder builder) {
		SuggestionProvider.suggestResource(Block.REGISTRY.keySet(), builder);
		return builder.buildFuture();
	}

	private void skipBackwards(Predicate<Character> condition) throws CommandSyntaxException {
		int cursor = this.reader.getCursor();
		do {
			this.reader.setCursor(--cursor);
		} while (cursor >= 0 && condition.test(this.reader.peek()));
		this.reader.setCursor(++cursor);
	}

	public void parseBlock() throws CommandSyntaxException {
		if (this.legacy) {
			skipBackwards(Character::isWhitespace);
			skipBackwards(ResourceUtils::isValidChar);
		}
		int cursor = this.reader.getCursor();
		this.id = IdentifierParser.parse(this.reader);
		if (!Block.REGISTRY.containsKey(this.id)) {
			this.reader.setCursor(cursor);
			throw INVALID_ID_EXCEPTION.createWithContext(this.reader, this.id.toString());
		}
		Block block = Block.REGISTRY.get(this.id);
		this.definition = block.stateDefinition();
		this.state = block.defaultState();
		if (this.legacy) {
			this.reader.skipWhitespace();
		}
	}

	public void parseProperties() throws CommandSyntaxException {
		if (this.legacy && Character.isDigit(this.reader.peek())) {
			this.suggestions = NO_SUGGESTIONS;
			int cursor = this.reader.getCursor();
			int metadata = this.reader.readInt();
			if (metadata < 0 || metadata > 15) {
				this.reader.setCursor(cursor);
				throw INVALID_DATA_EXCEPTION.createWithContext(this.reader, this.id, metadata);
			}
			this.state = this.definition.getBlock().getStateFromMetadata(metadata);
			for (Property<?> property : this.definition.properties()) {
				this.properties.put(property, this.state.get(property));
			}
		} else {
			if (this.legacy) {
				this.suggestions = this::suggestProperty;
			} else {
				this.reader.skip();
				this.suggestions = this::suggestPropertyOrEnd;
				this.reader.skipWhitespace();
			}
			while (this.reader.canRead() && (this.legacy ? !Character.isWhitespace(this.reader.peek()) : this.reader.peek() != ']')) {
				if (!this.legacy) {
					this.reader.skipWhitespace();
				}
				int propertyNameStart = this.reader.getCursor();
				String propertyName = this.reader.readString();
				Property<?> property = this.definition.getProperty(propertyName);
				if (property == null) {
					this.reader.setCursor(propertyNameStart);
					throw UNKNOWN_PROPERTY_EXCEPTION.createWithContext(this.reader, this.id.toString(), propertyName);
				}
				if (this.properties.containsKey(property)) {
					this.reader.setCursor(propertyNameStart);
					throw DUPLICATE_PROPERTY_EXCEPTION.createWithContext(this.reader, this.id.toString(), propertyName);
				}
				if (!this.legacy) {
					this.reader.skipWhitespace();
				}
				this.suggestions = this::suggestPropertyValueSeparator;
				if (!this.reader.canRead() || this.reader.peek() != '=') {
					throw NO_VALUE_EXCEPTION.createWithContext(this.reader, this.id.toString(), propertyName);
				}
				this.reader.skip();
				if (!this.legacy) {
					this.reader.skipWhitespace();
				}
				this.suggestions = builder -> BlockStateParser.suggestValues(builder, property).buildFuture();
				int valueNameStart = this.reader.getCursor();
				this.setValue(property, this.reader.readString(), valueNameStart);
				if (this.legacy) {
					this.suggestions = this::suggestNextProperty;
				} else {
					this.suggestions = this::suggestNextPropertyOrEnd;
					this.reader.skipWhitespace();
				}
				if (this.reader.canRead()) {
					if (this.reader.peek() == ',') {
						this.reader.skip();
						this.suggestions = this::suggestProperty;
					} else if (!this.legacy && this.reader.peek() != ']') {
						throw UNCLOSED_PROPERTY_EXCEPTION.createWithContext(this.reader);
					}
				}
			}
			if (!this.legacy) {
				if (!this.reader.canRead()) {
					throw UNCLOSED_PROPERTY_EXCEPTION.createWithContext(this.reader);
				}
				this.reader.skip();
			}
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

	public static String serialize(BlockState state) {
		StringBuilder sb = new StringBuilder(Block.REGISTRY.getKey(state.getBlock()).toString());
		if (!state.properties().isEmpty()) {
			boolean bl = false;
			for (Map.Entry<Property<?>, Comparable<?>> entry : state.values().entrySet()) {
				if (bl) {
					sb.append(',');
				}
				serializeProperty(sb, entry.getKey(), entry.getValue());
				bl = true;
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> void serializeProperty(StringBuilder sb, Property<T> property, Comparable<?> value) {
		sb.append(property.getName());
		sb.append('=');
		sb.append(property.getName((T)value));
	}

	public CompletableFuture<Suggestions> addSuggestions(SuggestionsBuilder builder) {
		return this.suggestions.apply(builder.createOffset(this.reader.getCursor()));
	}
}
