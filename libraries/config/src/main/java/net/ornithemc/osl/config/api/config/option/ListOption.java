package net.ornithemc.osl.config.api.config.option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import net.ornithemc.osl.config.api.config.option.validator.OptionValidators;

/**
 * This class provides a {@linkplain ModifiableOption} implementation for the
 * {@linkplain java.util.List List} type. This class is generic, thus you can
 * parameterize it as you would a {@code List}. The element type must be
 * provided when creating a {@code ListOption} instance - it is needed for
 * serialization.
 * <p>
 * To fulfill the contract established in {@code ModifiableOption}, this class
 * provides its own {@code add} and {@code remove} methods. Modifying the list
 * by calling methods on the {@code List} itself will throw an exception, as
 * {@link #get} returns an unmodifiable {@code List}.
 * <p>
 * For convenience, option serializers for this class are built into this API.
 * However, because this class is generic, you must register object serializers
 * for the generic type parameters instead.
 */
public class ListOption<T> extends ModifiableOption<List<T>> {

	protected final Class<T> elementType;
	protected final Predicate<T> elementValidator;

	public ListOption(Class<T> elementType, String name, String description) {
		this(elementType, name, description, Collections.emptyList());
	}

	public ListOption(Class<T> elementType, String name, String description, List<T> defaultValue) {
		super(name, description, defaultValue);

		this.elementType = elementType;
		this.elementValidator = OptionValidators.nonnull();
	}

	public ListOption(Class<T> elementType, String name, String description, List<T> defaultValue, Predicate<T> elementValidator) {
		super(name, description, defaultValue, OptionValidators.list(elementValidator));

		this.elementType = elementType;
		this.elementValidator = elementValidator;
	}

	public Class<T> getElementType() {
		return elementType;
	}

	@Override
	protected List<T> unmodifiable(List<T> value) {
		return Collections.unmodifiableList(value);
	}

	@Override
	protected List<T> modifiable(List<T> value) {
		return new ArrayList<>(value);
	}

	public boolean add(T e) {
		int size = get().size();

		modify(list -> {
			if (elementValidator.test(e)) {
				list.add(e);
			}
		});

		return get().size() != size;
	}

	public boolean remove(Object o) {
		int size = get().size();

		modify(list -> {
			list.remove(o);
		});

		return get().size() != size;
	}

	public boolean addAll(Collection<? extends T> c) {
		int size = get().size();

		modify(list -> {
			Collection<? extends T> elements = new ArrayList<>(c);
			elements.removeIf(elementValidator.negate());
			list.addAll(elements);
		});

		return get().size() != size;
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		int size = get().size();

		modify(list -> {
			Collection<? extends T> elements = new ArrayList<>(c);
			elements.removeIf(elementValidator.negate());
			list.addAll(index, elements);
		});

		return get().size() != size;
	}

	public boolean removeAll(Collection<?> c) {
		int size = get().size();

		modify(list -> {
			list.removeAll(c);
		});

		return get().size() != size;
	}

	public boolean retainAll(Collection<?> c) {
		int size = get().size();

		modify(list -> {
			list.retainAll(c);
		});

		return get().size() != size;
	}

	public void clear() {
		modify(List::clear);
	}

	public T set(int index, T element) {
		T prev = get().get(index);

		modify(list -> {
			if (elementValidator.test(element)) {
				list.set(index, element);
			}
		});

		return prev;
	}

	public void add(int index, T element) {
		modify(list -> list.add(index, element));
	}

	public T remove(int index) {
		T prev = get().get(index);

		modify(list ->{
			list.remove(index);
		});

		return prev;
	}
}
