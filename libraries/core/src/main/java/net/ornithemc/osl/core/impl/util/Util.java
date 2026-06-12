package net.ornithemc.osl.core.impl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class Util {

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
