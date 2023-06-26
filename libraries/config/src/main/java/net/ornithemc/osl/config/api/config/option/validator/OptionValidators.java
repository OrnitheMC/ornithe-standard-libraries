package net.ornithemc.osl.config.api.config.option.validator;

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
}
