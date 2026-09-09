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

	public static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
		int[] sectionSizes = new int[lists.size()];

		for (int layer = lists.size() - 1; layer >= 0; layer--) {
			sectionSizes[layer] = lists.get(layer).size();

			if (layer + 1 < lists.size()) {
				sectionSizes[layer] *= sectionSizes[layer + 1];
			}
		}

		int productSize = sectionSizes[0];
		List<List<T>> product = new ArrayList<>(productSize);

		for (int i = 0; i < productSize; i++) {
			product.add(new ArrayList<>(lists.size()));
		}

		for (int layer = 0; layer < lists.size(); layer++) {
			List<T> list = lists.get(layer);
			int sectionSize = sectionSizes[layer];
			int sectionRepeat = sectionSize / list.size();

			for (int index = 0; index < productSize; ) {
				for (T value : list) {
					for (int i = 0; i < sectionRepeat; i++) {
						product.get(index++).add(layer, value);
					}
				}
			}
		}

		return product;
	}
}
