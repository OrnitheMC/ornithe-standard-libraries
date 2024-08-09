package net.ornithemc.osl.config.api.config.option.validator;

import java.util.function.Predicate;

public class StringValidators {

	public static Predicate<String> startsWith(String prefix) {
		return value -> value.startsWith(prefix);
	}

	public static Predicate<String> endsWith(String suffix) {
		return value -> value.endsWith(suffix);
	}

	public static Predicate<String> regex(String regex) {
		return value -> value.matches(regex);
	}
}
