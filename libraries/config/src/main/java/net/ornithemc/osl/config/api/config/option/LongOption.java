package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class LongOption extends BaseOption<Long> {

	public LongOption(String name, String description, Long defaultValue) {
		super(name, description, defaultValue);
	}

	public LongOption(String name, String description, Long defaultValue, Predicate<Long> validator) {
		super(name, description, defaultValue, validator);
	}
}
