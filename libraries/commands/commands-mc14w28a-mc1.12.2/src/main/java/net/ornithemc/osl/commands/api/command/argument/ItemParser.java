package net.ornithemc.osl.commands.api.command.argument;

import java.util.concurrent.CompletableFuture;

import com.google.common.base.Function;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.api.ItemRegistry;
import net.ornithemc.osl.text.api.TextComponents;

public class ItemParser {

	public static final SimpleCommandExceptionType NO_TAGS_ALLOWED_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.item.tag.disallowed"));
	public static final DynamicCommandExceptionType INVALID_ID_EXCEPTION = new DynamicCommandExceptionType(id -> TextComponents.translatable("argument.item.id.invalid", id));
	private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> NO_SUGGESTIONS = SuggestionsBuilder::buildFuture;

	private final StringReader reader;
	private final boolean allowTag;

	private NamespacedIdentifier id = new Identifier("");
	private Item item;
	private NbtCompound nbt;
	private NamespacedIdentifier tag = new Identifier("");
//	private int cursor;
	private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions = NO_SUGGESTIONS;

	public ItemParser(StringReader reader, boolean allowTag) {
		this.reader = reader;
//		this.allowTag = allowTag;
		this.allowTag = false; // TODO
	}

	public NamespacedIdentifier getId() {
		return this.id;
	}

	public Item getItem() {
		return this.item;
	}

	public NbtCompound getNbt() {
		return this.nbt;
	}

	public NamespacedIdentifier getTag() {
		return this.tag;
	}

	public void parseItem() throws CommandSyntaxException {
		int resetCursor = this.reader.getCursor();

		this.id = IdentifierParser.parseIdentifier(this.reader);
		this.item = ItemRegistry.getItem(this.id);

		if (this.item == null) {
			this.reader.setCursor(resetCursor);
			throw INVALID_ID_EXCEPTION.createWithContext(this.reader, this.id.toString());
		}

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

	public void parseNbt() throws CommandSyntaxException {
		this.nbt = new SnbtParser(this.reader).readCompound();
	}

	public ItemParser parse() throws CommandSyntaxException {
		this.suggestions = this::suggestItemIdOrTag;

		if (this.reader.canRead() && this.reader.peek() == '#') {
			this.parseTag();
		} else {
			this.parseItem();
			this.suggestions = this::suggestNbtStart;
		}

		if (this.reader.canRead() && this.reader.peek() == '{') {
			this.suggestions = NO_SUGGESTIONS;
			this.parseNbt();
		}

		return this;
	}

	private CompletableFuture<Suggestions> suggestNbtStart(SuggestionsBuilder builder) {
		if (builder.getRemaining().isEmpty()) {
			builder.suggest(String.valueOf('{'));
		}

		return builder.buildFuture();
	}

	private CompletableFuture<Suggestions> suggestTag(SuggestionsBuilder builder) {
		return builder.buildFuture();
//		return SuggestionProvider.suggestResource(ItemTags.getAll().getKeys(), builder.createOffset(this.cursor));
	}

	private CompletableFuture<Suggestions> suggestItemIdOrTag(SuggestionsBuilder builder) {
		if (this.allowTag) {
//			SuggestionProvider.suggestResource(ItemTags.getAll().getKeys(), builder, String.valueOf('#'));
		}

		return SharedSuggestionProvider.suggestResource(ItemRegistry.identifierSet(), builder);
	}

	public CompletableFuture<Suggestions> addSuggestions(SuggestionsBuilder builder) {
		return this.suggestions.apply(builder.createOffset(this.reader.getCursor()));
	}
}
