package net.ornithemc.osl.config.api.config.option;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.ornithemc.osl.core.api.util.function.IOConsumer;

/**
 * This class is the basis for options of custom object types that are intended
 * to be modifiable. While the {@linkplain BaseOption} assumes the option type
 * to be unmodifiable (e.g. primitive types such as {@code boolean}s,
 * {@code int}s, {@code float}s, etc., or immutable objects like
 * {@linkplain java.lang.String String}s, {@linkplain java.util.UUID UUID}s,
 * etc.) there are plenty of object types that are mutable, like
 * {@linkplain java.util.Collection Collection} and its subtypes, or OSL Core's
 * {@linkplain net.ornithemc.osl.core.api.events.Event Event}. This class
 * provides an {@linkplain Option} implementation that is intended to be used
 * for these types.
 * <p>
 * Its implementations for {@link #getDefault} and {@link #get} return
 * unmodifiable views of the default and current value respectively, while its
 * implementation for {@link #set} first converts the given value to a
 * modifiable view before setting it as the current value. It does these
 * conversions through the {@link #modifiable} and {@link #unmodifiable}
 * methods. It also provides a {@link #modify} method for modifying the current
 * value of the option, so that it does not need to be replaced entirely.
 */
public abstract class ModifiableOption<T> extends BaseOption<T> {

	protected ModifiableOption(String name, String description, T defaultValue) {
		super(name, description, defaultValue);
	}

	protected ModifiableOption(String name, String description, T defaultValue, Predicate<T> validator) {
		super(name, description, defaultValue, validator);
	}

	/**
	 * Returns an unmodifiable view of the default value of this option.
	 * 
	 * @return the default value of this option
	 * @see #unmodifiable
	 */
	@Override
	public T getDefault() {
		return unmodifiable(super.getDefault());
	}

	/**
	 * Returns an unmodifiable view of the current value of this option. Changes to
	 * the current value of this option should be applied through
	 * {@linkplain #modify}, or {@linkplain #set} to replace the current value
	 * entirely.
	 * 
	 * @return the current value of this option
	 * @see #modifiable
	 */
	@Override
	public T get() {
		return unmodifiable(super.get());
	}

	/**
	 * Sets this option to a modifiable view of the given value. Changes to the
	 * current value of this option should be applied through {@linkplain #modify},
	 * or {@linkplain #set} to replace the current value entirely.
	 * 
	 * @see #modifiable
	 */
	@Override
	public void set(T value) {
		super.set(modifiable(value));
	}

	/**
	 * Returns an unmodifiable view of the given value. The return value of this
	 * method should not be the same object as the given value, but should be equal
	 * to it. In general, the expression <blockquote>
	 * 
	 * <pre>
	 * unmodifiable(value) != value
	 * </pre>
	 * 
	 * </blockquote> must be true, and the expression: <blockquote>
	 * 
	 * <pre>
	 * unmodifiable(value).equals(value)
	 * </pre>
	 * 
	 * </blockquote> must be true.
	 * 
	 * @return an unmodifiable view of the given value
	 */
	protected abstract T unmodifiable(T value);

	/**
	 * Returns a modifiable view of the given value. The return value of this method
	 * should not be the same object as the given value, but should be equal to it.
	 * In general, the expression <blockquote>
	 * 
	 * <pre>
	 * modifiable(value) != value
	 * </pre>
	 * 
	 * </blockquote> must be true, and the expression: <blockquote>
	 * 
	 * <pre>
	 * modifiable(value).equals(value)
	 * </pre>
	 * 
	 * </blockquote> must be true.
	 * 
	 * @return a modifiable view of the given value
	 */
	protected abstract T modifiable(T value);

	/**
	 * modifies the current value of this option
	 * 
	 * @return whether this option's current value was modified.
	 */
	public void modify(Consumer<T> modifier) {
		// save the state of the current value
		// in case the modified value is invalid
		T current = get();
		T modified = super.get();

		modifier.accept(modified);

		// the set method checks if the current and new
		// values are not equal before it sets the new
		// value, but we allow the current value to be
		// modified, so we must first set the value back
		// to the saved state
		set(current);
		set(modified);
	}

	/**
	 * modifies the current value of this option or throws an IO exception
	 * 
	 * @return whether this option's current value was modified.
	 */
	public void modifyIO(IOConsumer<T> modifier) throws IOException {
		// save the state of the current value
		// in case the modified value is invalid
		T current = get();
		T modified = super.get();

		modifier.accept(modified);

		// the set method checks if the current and new
		// values are not equal before it sets the new
		// value, but we allow the current value to be
		// modified, so we must first set the value back
		// to the saved state
		set(current);
		set(modified);
	}
}
