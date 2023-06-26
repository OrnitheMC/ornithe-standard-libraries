package net.ornithemc.osl.config.api.config.option;

import java.util.Objects;
import java.util.function.Predicate;

import net.ornithemc.osl.config.api.config.option.validator.OptionValidators;

public abstract class BaseOption<T> implements Option {

	protected final String name;
	protected final String description;
	protected final T defaultValue;
	protected final Predicate<T> validator;

	private T value;
	private boolean loaded;

	protected BaseOption(String name, String description, T defaultValue) {
		this(name, description, defaultValue, OptionValidators.nonnull());
	}

	protected BaseOption(String name, String description, T defaultValue, Predicate<T> validator) {
		this.name = name;
		this.description = description;
		this.defaultValue = defaultValue;
		this.validator = validator;

		if (!this.validator.test(this.defaultValue)) {
			throw new IllegalArgumentException("invalid default value given!");
		}

		this.value = null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean isDefault() {
		return Objects.equals(get(), getDefault());
	}

	@Override
	public void reset() {
		set(getDefault());
	}

	@Override
	public void load() {
		loaded = true;
	}

	@Override
	public void unload() {
		loaded = false;
	}

	public T getDefault() {
		return defaultValue;
	}

	public T get() {
		requireLoaded();

		return value;
	}

	public void set(T newValue) {
		requireLoaded();

		if (!Objects.equals(value, newValue) && validator.test(newValue)) {
			value = newValue;
		}
	}

	public void requireLoaded() {
		if (!loaded) {
			throw new IllegalStateException("this option is not loaded!");
		}
	}
}
