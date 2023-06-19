package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class BooleanOption extends Option<Boolean> {

	public BooleanOption(String name, boolean defaultValue) {
		super(name, defaultValue);
	}

	public BooleanOption(String name, boolean defaultValue, Predicate<Boolean> validator) {
		super(name, defaultValue, validator);
	}
}
