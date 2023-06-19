package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class StringOption extends Option<String> {

	public StringOption(String name, String defaultValue) {
		super(name, defaultValue);
	}

	public StringOption(String name, String defaultValue, Predicate<String> validator) {
		super(name, defaultValue, validator);
	}
}
