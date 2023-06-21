package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class StringOption extends BaseOption<String> {

	public StringOption(String name, String description, String defaultValue) {
		super(name, description, defaultValue);
	}

	public StringOption(String name, String description, String defaultValue, Predicate<String> validator) {
		super(name, description, defaultValue, validator);
	}
}
