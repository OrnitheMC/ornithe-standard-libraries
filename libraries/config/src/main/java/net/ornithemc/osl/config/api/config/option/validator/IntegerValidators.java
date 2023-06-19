package net.ornithemc.osl.config.api.config.option.validator;

import java.util.function.Predicate;

public class IntegerValidators {

	public static Predicate<Integer> min(int min) {
		return value -> value >= min;
	}

	public static Predicate<Integer> max(int max) {
		return value -> value <= max;
	}

	public static Predicate<Integer> minmax(int min, int max) {
		return value -> value >= min && value <= max;
	}
}
