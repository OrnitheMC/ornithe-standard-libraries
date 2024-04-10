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

	/**
	 * @return the default value of this option
	 */
	public T getDefault() {
		return defaultValue;
	}

	/**
	 * @return the current value of this option
	 */
	public T get() {
		requireLoaded();

		return value;
	}

	/**
	 * @return whether this option's current value has changed
	 */
	public boolean set(T newValue) {
		requireLoaded();

		if (!Objects.equals(value, newValue) && validator.test(newValue)) {
			value = newValue;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Throws an exception if this option is not loaded.
	 * Options are loaded while their parent config's scope is loaded.
	 */
	public void requireLoaded() {
		if (!loaded) {
			throw new IllegalStateException("this option is not loaded!");
		}
	}
}
