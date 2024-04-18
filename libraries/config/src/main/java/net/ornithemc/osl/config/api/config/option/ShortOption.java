package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class ShortOption extends BaseOption<Short> {

	public ShortOption(String name, String description, Short defaultValue) {
		super(name, description, defaultValue);
	}

	public ShortOption(String name, String description, Short defaultValue, Predicate<Short> validator) {
		super(name, description, defaultValue, validator);
	}
}
