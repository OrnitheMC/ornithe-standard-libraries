package net.ornithemc.osl.core.impl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public final class Util {

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
