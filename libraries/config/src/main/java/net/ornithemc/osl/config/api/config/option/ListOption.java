package net.ornithemc.osl.config.api.config.option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import net.ornithemc.osl.config.api.config.option.validator.OptionValidators;

/**
 * This class provides a {@linkplain ModifiableOption} implementation for the
 * {@linkplain java.util.List List} type. This class is generic, thus you can
 * parameterize it as you would a {@code List}. The element type must be
 * provided when creating a {@code ListOption} instance - it is needed for
 * serialization.
 * <p>
 * This class implements the {@code List} interface to simplify querying and
 * modifying the value of this option. To fulfill the contract established in
 * {@code ModifiableOption}, the lists returned by this class'
 * {@link #getDefault} and {@link #get} methods return unmodifiable views of the
 * default and current value of this option. Adding and removing elements to the
 * current value should be done by calling methods from the {@code List}
 * interface instead.
 * <p>
 * For convenience, option serializers for this class are built into this API.
 * However, because this class is generic, you must register object serializers
 * for the generic type parameters instead.
 */
public class ListOption<T> extends ModifiableOption<List<T>> implements List<T> {

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

	@Override
	public int size() {
		return get().size();
	}

	@Override
	public boolean isEmpty() {
		return get().isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return get().contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return get().iterator();
	}

	@Override
	public Object[] toArray() {
		return get().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return get().toArray(a);
	}

	@Override
	public boolean add(T e) {
		int size = size();

		modify(list -> {
			list.add(e);
		});

		return size != size();
	}

	@Override
	public boolean remove(Object o) {
		int size = size();

		modify(list -> {
			list.remove(o);
		});

		return size != size();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return get().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		int size = size();

		modify(list -> {
			list.addAll(c);
		});

		return size != size();
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		int size = size();

		modify(list -> {
			list.addAll(index, c);
		});

		return size != size();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		int size = size();

		modify(list -> {
			list.removeAll(c);
		});

		return size != size();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		int size = size();

		modify(list -> {
			list.retainAll(c);
		});

		return size != size();
	}

	@Override
	public void clear() {
		modify(List::clear);
	}

	@Override
	public T get(int index) {
		return get().get(index);
	}

	@Override
	public T set(int index, T element) {
		T prev = get(index);

		modify(list -> {
			list.set(index, element);
		});

		return prev;
	}

	@Override
	public void add(int index, T element) {
		modify(list -> {
			list.add(index, element);
		});
	}

	@Override
	public T remove(int index) {
		T prev = get(index);

		modify(list -> {
			list.remove(index);
		});

		return prev;
	}

	@Override
	public int indexOf(Object o) {
		return get().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return get().lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return get().listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return get().listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return get().subList(fromIndex, toIndex);
	}
}
