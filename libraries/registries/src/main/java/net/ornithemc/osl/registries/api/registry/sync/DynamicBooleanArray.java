package net.ornithemc.osl.registries.api.registry.sync;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.ornithemc.osl.registries.impl.registry.sync.DynamicArrays;

/**
 * This class can be used to dynamically grow boolean arrays ({@code boolean[]})
 * stored in fields. It holds no reference to the array, instead relying on a
 * {@linkplain Supplier} to retrieve the array reference, and a
 * {@linkplain Consumer} to store an array reference. Apart from the usual
 * {@linkplain #length()}, {@linkplain #get(int)}, and
 * {@linkplain #set(int, boolean)} methods, there are the
 * {@linkplain #add(int, boolean)} and {@linkplain #addAll(DynamicBooleanArray)}
 * methods, which grow the array if necessary.
 */
public final class DynamicBooleanArray {

	public static DynamicBooleanArray of(int capacity) {
		boolean[][] array = new boolean[][] { new boolean[capacity] };

		return new DynamicBooleanArray(() -> array[0], a -> array[0] = a);
	}

	public static DynamicBooleanArray of(Supplier<boolean[]> getter, Consumer<boolean[]> setter) {
		return new DynamicBooleanArray(getter, setter);
	}

	private final Supplier<boolean[]> getter;
	private final Consumer<boolean[]> setter;

	private DynamicBooleanArray(Supplier<boolean[]> getter, Consumer<boolean[]> setter) {
		this.getter = getter;
		this.setter = setter;
	}

	public int capacity() {
		return this.getter.get().length;
	}

	public int length() {
		return this.capacity();
	}

	public boolean get(int index) {
		return this.getter.get()[index];
	}

	public boolean set(int index, boolean value) {
		return this.getter.get()[index] = value;
	}

	public boolean add(int index, boolean value) {
		int capacity = index + 1;

		if (this.capacity() < capacity) {
			this.grow(capacity);
		}

		return this.set(index, value);
	}

	public void addAll(DynamicBooleanArray array) {
		int capacity = array.length();

		if (this.capacity() < capacity) {
			this.grow(capacity);
		}

		System.arraycopy(array.getter.get(), 0, this.getter.get(), 0, array.length());
	}

	public void clear() {
		Arrays.fill(this.getter.get(), false);
	}

	public void grow(int capacity) {
		this.setter.accept(grow(this.getter.get(), capacity));
	}

	public static boolean[] grow(boolean[] array, int capacity) {
		if (capacity > array.length) {
			array = Arrays.copyOf(array, DynamicArrays.newLength(array.length, capacity));
		}

		return array;
	}
}
