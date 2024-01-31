package net.ornithemc.osl.commands.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.base.Strings;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.resource.Identifier;

public interface SuggestionProvider {

	Collection<String> getPlayerNames();

	default Collection<String> getSelectedEntities() {
		return Collections.emptyList();
	}

	Collection<String> getTeams();

	CompletableFuture<Suggestions> suggest(CommandContext<SuggestionProvider> ctx, SuggestionsBuilder builder);

	Collection<Coordinate> getCoordinates(boolean absolute);

	boolean hasPermissions(int permissions);

	static <T> void filterResources(Iterable<T> it, String id, Function<T, Identifier> idGetter, Consumer<T> action) {
		boolean hasColon = id.indexOf(':') > -1;
		for (T object : it) {
			Identifier identifier = idGetter.apply(object);
			if (hasColon) {
				String string = identifier.toString();
				if (!string.startsWith(id))
					continue;
				action.accept(object);
				continue;
			}
			if (!identifier.getNamespace().startsWith(id)
					&& (!identifier.getNamespace().equals("minecraft") || !identifier.getPath().startsWith(id)))
				continue;
			action.accept(object);
		}
	}

	static <T> void filterResources(Iterable<T> it, String id1, String id2, Function<T, Identifier> idGetter, Consumer<T> action) {
		if (id1.isEmpty()) {
			it.forEach(action);
		} else {
			String prefix = Strings.commonPrefix(id1, id2);
			if (!prefix.isEmpty()) {
				String target = id1.substring(prefix.length());
				SuggestionProvider.filterResources(it, target, idGetter, action);
			}
		}
	}

	static CompletableFuture<Suggestions> suggestResource(Iterable<Identifier> ids, SuggestionsBuilder builder, String id2) {
		String target = builder.getRemaining().toLowerCase(Locale.ROOT);
		SuggestionProvider.filterResources(ids, target, id2, id -> id, resource -> builder.suggest(id2 + resource));
		return builder.buildFuture();
	}

	static CompletableFuture<Suggestions> suggestResource(Iterable<Identifier> ids, SuggestionsBuilder builder) {
		String target = builder.getRemaining().toLowerCase(Locale.ROOT);
		SuggestionProvider.filterResources(ids, target, id -> id, resource -> builder.suggest(resource.toString()));
		return builder.buildFuture();
	}

	static <T> CompletableFuture<Suggestions> suggestResource(Iterable<T> ids, SuggestionsBuilder builder, Function<T, Identifier> idGetter, Function<T, Message> suggestionGetter) {
		String target = builder.getRemaining().toLowerCase(Locale.ROOT);
		SuggestionProvider.filterResources(ids, target, idGetter, resource -> builder.suggest(((Identifier) idGetter.apply(resource)).toString(), suggestionGetter.apply(resource)));
		return builder.buildFuture();
	}

	static CompletableFuture<Suggestions> suggestResource(Stream<Identifier> ids, SuggestionsBuilder builder) {
		return SuggestionProvider.suggestResource(ids::iterator, builder);
	}

	static <T> CompletableFuture<Suggestions> suggestResource(Stream<T> ids, SuggestionsBuilder builder, Function<T, Identifier> idGetter, Function<T, Message> suggestionGetter) {
		return SuggestionProvider.suggestResource(ids::iterator, builder, idGetter, suggestionGetter);
	}

	static CompletableFuture<Suggestions> suggestCoordinates(String s, Collection<Coordinate> coordinates, SuggestionsBuilder builder, Predicate<String> validator) {
		ArrayList<String> suggestions;
		block4: {
			String[] strings;
			block5: {
				block3: {
					suggestions = new ArrayList<>();
					if (!Strings.isNullOrEmpty(s))
						break block3;
					for (Coordinate coordinate : coordinates) {
						String string = coordinate.x + " " + coordinate.y + " " + coordinate.z;
						if (!validator.test(string))
							continue;
						suggestions.add(coordinate.x);
						suggestions.add(coordinate.x + " " + coordinate.y);
						suggestions.add(string);
					}
					break block4;
				}
				strings = s.split(" ");
				if (strings.length != 1)
					break block5;
				for (Coordinate coordinate2 : coordinates) {
					String string2 = strings[0] + " " + coordinate2.y + " " + coordinate2.z;
					if (!validator.test(string2))
						continue;
					suggestions.add(strings[0] + " " + coordinate2.y);
					suggestions.add(string2);
				}
				break block4;
			}
			if (strings.length != 2)
				break block4;
			for (Coordinate coordinate2 : coordinates) {
				String string2 = strings[0] + " " + strings[1] + " " + coordinate2.z;
				if (!validator.test(string2))
					continue;
				suggestions.add(string2);
			}
		}
		return SuggestionProvider.suggestMatching(suggestions, builder);
	}

	static CompletableFuture<Suggestions> suggestHorizontalCoordinates(String s, Collection<Coordinate> coordinates, SuggestionsBuilder builder, Predicate<String> validator) {
		ArrayList<String> suggestions;
		block3: {
			block2: {
				suggestions = new ArrayList<>();
				if (!Strings.isNullOrEmpty(s))
					break block2;
				for (Coordinate coordinate : coordinates) {
					String string = coordinate.x + " " + coordinate.z;
					if (!validator.test(string))
						continue;
					suggestions.add(coordinate.x);
					suggestions.add(string);
				}
				break block3;
			}
			String[] strings = s.split(" ");
			if (strings.length != 1)
				break block3;
			for (Coordinate coordinate2 : coordinates) {
				String string2 = strings[0] + " " + coordinate2.z;
				if (!validator.test(string2))
					continue;
				suggestions.add(string2);
			}
		}
		return SuggestionProvider.suggestMatching(suggestions, builder);
	}

	static CompletableFuture<Suggestions> suggestMatching(Iterable<String> suggestions,SuggestionsBuilder builder) {
		String target = builder.getRemaining().toLowerCase(Locale.ROOT);
		for (String suggestion : suggestions) {
			if (suggestion.toLowerCase(Locale.ROOT).startsWith(target)) {
				builder.suggest(suggestion);
			}
		}
		return builder.buildFuture();
	}

	static CompletableFuture<Suggestions> suggestMatching(Stream<String> suggestions, SuggestionsBuilder builder) {
		String target = builder.getRemaining().toLowerCase(Locale.ROOT);
		suggestions.filter(s -> s.toLowerCase(Locale.ROOT).startsWith(target)).forEach(builder::suggest);
		return builder.buildFuture();
	}

	static CompletableFuture<Suggestions> suggestMatching(String[] suggestions, SuggestionsBuilder builder) {
		String target = builder.getRemaining().toLowerCase(Locale.ROOT);
		for (String suggestion : suggestions) {
			if (suggestion.toLowerCase(Locale.ROOT).startsWith(target)) {
				builder.suggest(suggestion);
			}
		}
		return builder.buildFuture();
	}

	public class Coordinate {

		public static final Coordinate DEFAULT_LOCAL = new Coordinate("^", "^", "^");
		public static final Coordinate DEFAULT_GLOBAL = new Coordinate("~", "~", "~");

		public final String x;
		public final String y;
		public final String z;

		public Coordinate(String x, String y, String z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}
