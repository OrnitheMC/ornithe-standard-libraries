package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class IntegerOption extends BaseOption<Integer> {

	public IntegerOption(String name, String description, Integer defaultValue) {
		super(name, description, defaultValue);
	}

	public IntegerOption(String name, String description, Integer defaultValue, Predicate<Integer> validator) {
		super(name, description, defaultValue, validator);
	}
}
