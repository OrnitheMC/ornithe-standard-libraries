package net.ornithemc.osl.core.impl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public final class Util {

	public static String pascalCaseToSnakeCase(String s) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char chr = s.charAt(i);

			if (Character.isUpperCase(chr)) {
				chr = Character.toLowerCase(chr);

				// add _ if prev char is not upper case or if the next char is not upper case
				// (this keeps abbreviations like TNT together in IDs like TNTMinecart)
				if (i != 0 && (!Character.isUpperCase(s.charAt(i - 1)) || (i != s.length() - 1 && !Character.isUpperCase(s.charAt(i + 1))))) {
					sb.append('_');
				}
			}

			sb.append(chr);
		}

		return sb.toString();
	}

	public static String snakeCaseToPascalCase(String s) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char chr = s.charAt(i);

			if (chr != '_') {
				// convert first char and every char after _
				if (i == 0 || s.charAt(i - 1) == '_') {
					chr = Character.toUpperCase(chr);
				}

				sb.append(chr);
			}
		}

		return sb.toString();
	}

	public static String makeTranslationKey(NamespacedIdentifier identifier) {
		return identifier.namespace() + "." + identifier.identifier().replace('/', '.');
	}

	public static String makeTranslationKey(String prefix, NamespacedIdentifier identifier) {
		return prefix + "." + makeTranslationKey(identifier);
	}

	public static String makeTranslationKey(String prefix, NamespacedIdentifier identifier, String suffix) {
		return makeTranslationKey(prefix, identifier) + "." + suffix;
	}

	public static <V> CompletableFuture<List<V>> sequence(List<? extends CompletableFuture<? extends V>> futures) {
		List<V> results = new ArrayList<>(futures.size());

		CompletableFuture<?>[] sequence = new CompletableFuture[futures.size()];
		CompletableFuture<Void> failure = new CompletableFuture<>();

		futures.forEach(future -> {
			int i = results.size();
			results.add(null);

			sequence[i] = future.whenComplete((result, exception) -> {
				if (exception != null) {
					failure.completeExceptionally(exception);
				} else {
					results.set(i, result);
				}
			});
		});

		return CompletableFuture.allOf(sequence).applyToEither(failure, v -> results);
	}
}
