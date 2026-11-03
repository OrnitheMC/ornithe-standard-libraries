package net.ornithemc.osl.commands.api.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface SharedSuggestionProvider {

	Collection<String> getPlayerNames();

	default Collection<String> getSelectedEntities() {
		return Collections.emptyList();
	}

	Collection<String> getTeams();

	CompletableFuture<Suggestions> requestSuggestions(CommandContext<SharedSuggestionProvider> ctx, SuggestionsBuilder builder);

	Collection<TextCoordinates> getRelevantCoordinates(boolean absolute);

	boolean hasPermissions(int permissions);

	static <T> void filterResources(Iterable<T> values, String target, Function<T, ? extends NamespacedIdentifier> converter, Consumer<T> consumer) {
		boolean hasNamespace = target.indexOf(':') > -1;

		for (T value : values) {
			NamespacedIdentifier key = converter.apply(value);

			if (hasNamespace) {
				String name = key.toString();

				if (name.startsWith(target)) {
					consumer.accept(value);
				}
			} else if (key.namespace().startsWith(target) || key.identifier().startsWith(target)) {
				consumer.accept(value);
			}
		}
	}

	static <T> void filterResources(Iterable<T> values, String target, String prefix, Function<T, ? extends NamespacedIdentifier> converter, Consumer<T> consumer) {
		if (target.isEmpty()) {
			values.forEach(consumer);
		} else {
			String commonPrefix = Strings.commonPrefix(target, prefix);

			if (!commonPrefix.isEmpty()) {
				String strippedTarget = target.substring(commonPrefix.length());
				filterResources(values, strippedTarget, converter, consumer);
			}
		}
	}

	static CompletableFuture<Suggestions> suggestResource(Iterable<? extends NamespacedIdentifier> keys, SuggestionsBuilder builder, String prefix) {
		String target = builder.getRemaining().toLowerCase(Locale.ROOT);
		filterResources(keys, target, prefix, id -> id, resource -> builder.suggest(prefix + resource));
		return builder.buildFuture();
	}

	static CompletableFuture<Suggestions> suggestResource(Iterable<? extends NamespacedIdentifier> keys, SuggestionsBuilder builder) {
		String target = builder.getRemaining().toLowerCase(Locale.ROOT);
		filterResources(keys, target, id -> id, resource -> builder.suggest(resource.toString()));
		return builder.buildFuture();
	}

	static <T> CompletableFuture<Suggestions> suggestResource(Iterable<T> values, SuggestionsBuilder builder, Function<T, ? extends NamespacedIdentifier> key, Function<T, Message> tooltip) {
		String target = builder.getRemaining().toLowerCase(Locale.ROOT);
		filterResources(values, target, key, resource -> builder.suggest(key.apply(resource).toString(), tooltip.apply(resource)));
		return builder.buildFuture();
	}

	static CompletableFuture<Suggestions> suggestResource(Stream<NamespacedIdentifier> keys, SuggestionsBuilder builder) {
		return suggestResource(keys::iterator, builder);
	}

	static <T> CompletableFuture<Suggestions> suggestResource(Stream<T> values, SuggestionsBuilder builder, Function<T, ? extends NamespacedIdentifier> key, Function<T, Message> tooltip) {
		return suggestResource(values::iterator, builder, key, tooltip);
	}

	static CompletableFuture<Suggestions> suggestCoordinates(String s, Collection<TextCoordinates> coordinates, SuggestionsBuilder builder, Predicate<String> validator) {
		List<String> result = new ArrayList<>();

		if (Strings.isNullOrEmpty(s)) {
			for (TextCoordinates coordinate : coordinates) {
				String fullValue = coordinate.x + " " + coordinate.y + " " + coordinate.z;

				if (validator.test(fullValue)) {
					result.add(coordinate.x);
					result.add(coordinate.x + " " + coordinate.y);
					result.add(fullValue);
				}
			}
		} else {
			String[] fields = s.split(" ");

			if (fields.length == 1) {
				for (TextCoordinates coordinate : coordinates) {
					String fullValue = fields[0] + " " + coordinate.y + " " + coordinate.z;

					if (validator.test(fullValue)) {
						result.add(fields[0] + " " + coordinate.y);
						result.add(fullValue);
					}
				}
			} else if (fields.length == 2) {
				for (TextCoordinates coordinate : coordinates) {
					String fullValue = fields[0] + " " + fields[1] + " " + coordinate.z;

					if (validator.test(fullValue)) {
						result.add(fullValue);
					}
				}
			}
		}

		return suggestMatching(result, builder);
	}

	static CompletableFuture<Suggestions> suggestHorizontalCoordinates(String s, Collection<TextCoordinates> coordinates, SuggestionsBuilder builder, Predicate<String> validator) {
		List<String> result = new ArrayList<>();

		if (Strings.isNullOrEmpty(s)) {
			for (TextCoordinates coordinate : coordinates) {
				String fullValue = coordinate.x + " " + coordinate.z;

				if (validator.test(fullValue)) {
					result.add(coordinate.x);
					result.add(fullValue);
				}
			}
		} else {
			String[] fields = s.split(" ");

			if (fields.length == 1) {
				for (TextCoordinates coordinate : coordinates) {
					String fullValue = fields[0] + " " + coordinate.z;

					if (validator.test(fullValue)) {
						result.add(fullValue);
					}
				}
			}
		}

		return suggestMatching(result, builder);
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

	class TextCoordinates {

		public static final TextCoordinates DEFAULT_LOCAL = new TextCoordinates("^", "^", "^");
		public static final TextCoordinates DEFAULT_GLOBAL = new TextCoordinates("~", "~", "~");

		public final String x;
		public final String y;
		public final String z;

		public TextCoordinates(String x, String y, String z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}
