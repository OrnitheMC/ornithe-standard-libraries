package net.ornithemc.osl.config.api.config.option.validator;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class OptionValidators {

	public static <T> Predicate<T> always() {
		return value -> true;
	}

	public static <T> Predicate<T> never() {
		return value -> false;
	}

	public static <T> Predicate<T> nonnull() {
		return value -> value != null;
	}

	public static <T> Predicate<List<T>> list(Predicate<T> elementValidator) {
		return value -> {
			for (int i = 0; i < value.size(); i++) {
				if (!elementValidator.test(value.get(i))) {
					return false;
				}
			}

			return true;
		};
	}

	public static <K, V> Predicate<Map<K, V>> map(BiPredicate<K, V> entryValidator) {
		return value -> {
			for (Map.Entry<K, V> entry : value.entrySet()) {
				if (!entryValidator.test(entry.getKey(), entry.getValue())) {
					return false;
				}
			}

			return true;
		};
	}
}
