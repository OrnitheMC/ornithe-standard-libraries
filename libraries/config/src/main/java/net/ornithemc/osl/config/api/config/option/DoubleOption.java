package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class DoubleOption extends BaseOption<Double> {

	public DoubleOption(String name, String description, Double defaultValue) {
		super(name, description, defaultValue);
	}

	public DoubleOption(String name, String description, Double defaultValue, Predicate<Double> validator) {
		super(name, description, defaultValue, validator);
	}
}
