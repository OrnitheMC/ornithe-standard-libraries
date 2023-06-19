package net.ornithemc.osl.config.api.config.option;

import java.util.Objects;
import java.util.function.Predicate;

import net.ornithemc.osl.config.api.config.option.validator.OptionValidators;

public abstract class Option<T> {

	protected final String name;
	protected final T defaultValue;
	protected final Predicate<T> validator;

	private T value;

	protected Option(String name, T defaultValue) {
		this(name, defaultValue, OptionValidators.nonnull());
	}

	protected Option(String name, T defaultValue, Predicate<T> validator) {
		this.name = name; 
		this.defaultValue = defaultValue;
		this.validator = validator;

		if (!this.validator.test(this.defaultValue)) {
			throw new IllegalArgumentException("invalid default value given!");
		}

		this.value = null;
	}

	public String getName() {
		return name;
	}

	public T getDefault() {
		return defaultValue;
	}

	public T get() {
		return value;
	}

	public void set(T newValue) {
		if (!Objects.equals(value, newValue) && validator.test(newValue)) {
			value = newValue;
		}
	}

	public void reset() {
		set(getDefault());
	}
}
