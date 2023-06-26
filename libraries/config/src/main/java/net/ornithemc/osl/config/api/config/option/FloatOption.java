package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class FloatOption extends BaseOption<Float> {

	public FloatOption(String name, String description, Float defaultValue) {
		super(name, description, defaultValue);
	}

	public FloatOption(String name, String description, Float defaultValue, Predicate<Float> validator) {
		super(name, description, defaultValue, validator);
	}
}
