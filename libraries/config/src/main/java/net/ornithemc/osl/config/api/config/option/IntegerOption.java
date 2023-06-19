package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class IntegerOption extends Option<Integer> {

	public IntegerOption(String name, Integer defaultValue) {
		super(name, defaultValue);
	}

	public IntegerOption(String name, Integer defaultValue, Predicate<Integer> validator) {
		super(name, defaultValue, validator);
	}
}
