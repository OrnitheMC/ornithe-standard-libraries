package net.ornithemc.osl.registries.api.registry.sync;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.ornithemc.osl.registries.impl.registry.sync.DynamicArrays;

/**
 * This class can be used to dynamically grow object arrays ({@code T[]}) stored
 * in fields. It holds no reference to the array, instead relying on a
 * {@linkplain Supplier} to retrieve the array reference, and a
 * {@linkplain Consumer} to store an array reference. Apart from the usual
 * {@linkplain #length()}, {@linkplain #get(int)}, and {@linkplain #set(int, T)}
 * methods, there are the {@linkplain #add(int, T)} and
 * {@linkplain #addAll(DynamicArray)} methods, which grow the array if
 * necessary.
 * 
 * @param <T> the array type.
 */
public final class DynamicArray<T> {

	public static <T> DynamicArray<T> of(int capacity) {
		@SuppressWarnings("unchecked")
		T[][] array = (T[][]) new Object[][] { new Object[capacity] };

		return new DynamicArray<>(() -> array[0], a -> array[0] = a);
	}

	public static <T> DynamicArray<T> of(Supplier<T[]> getter, Consumer<T[]> setter) {
		return new DynamicArray<>(getter, setter);
	}

	private final Supplier<T[]> getter;
	private final Consumer<T[]> setter;

	private DynamicArray(Supplier<T[]> getter, Consumer<T[]> setter) {
		this.getter = getter;
		this.setter = setter;
	}

	public int capacity() {
		return this.getter.get().length;
	}

	public int length() {
		return length(this.getter.get());
	}

	public T get(int index) {
		return this.getter.get()[index];
	}

	public T set(int index, T value) {
		return this.getter.get()[index] = value;
	}

	public T add(int index, T value) {
		int capacity = index + 1;

		if (this.capacity() < capacity) {
			this.grow(capacity);
		}

		return this.set(index, value);
	}

	public void addAll(DynamicArray<T> array) {
		int capacity = array.length();

		if (this.capacity() < capacity) {
			this.grow(capacity);
		}

		System.arraycopy(array.getter.get(), 0, this.getter.get(), 0, array.length());
	}

	public void clear() {
		Arrays.fill(this.getter.get(), null);
	}

	public void grow(int capacity) {
		this.setter.accept(grow(this.getter.get(), capacity));
	}

	public static <T> int length(T[] array) {
		for (int index = array.length - 1; index >= 0; index--) {
			if (array[index] != null) {
				return index + 1;
			}
		}

		return 0;
	}

	public static <T> T[] grow(T[] array, int capacity) {
		if (capacity > array.length) {
			array = Arrays.copyOf(array, DynamicArrays.newLength(array.length, capacity));
		}

		return array;
	}
}
