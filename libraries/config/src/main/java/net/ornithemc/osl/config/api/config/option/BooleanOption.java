package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class BooleanOption extends BaseOption<Boolean> {

	public BooleanOption(String name, String description, boolean defaultValue) {
		super(name, description, defaultValue);
	}

	public BooleanOption(String name, String description, boolean defaultValue, Predicate<Boolean> validator) {
		super(name, description, defaultValue, validator);
	}
}
