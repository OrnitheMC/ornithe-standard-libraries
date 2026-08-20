package net.ornithemc.osl.registries.api.registry.sync;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.ornithemc.osl.registries.impl.registry.sync.DynamicArrays;

/**
 * This class can be used to dynamically grow int arrays ({@code int[]}) stored
 * in fields. It holds no reference to the array, instead relying on a
 * {@linkplain Supplier} to retrieve the array reference, and a
 * {@linkplain Consumer} to store an array reference. Apart from the usual
 * {@linkplain #length()}, {@linkplain #get(int)}, and
 * {@linkplain #set(int, int)} methods, there are the
 * {@linkplain #add(int, int)} and {@linkplain #addAll(DynamicIntArray)}
 * methods, which grow the array if necessary.
 */
public final class DynamicIntArray {

	public static DynamicIntArray of(int capacity) {
		int[][] array = new int[][] { new int[capacity] };

		return new DynamicIntArray(() -> array[0], a -> array[0] = a);
	}

	public static DynamicIntArray of(Supplier<int[]> getter, Consumer<int[]> setter) {
		return new DynamicIntArray(getter, setter);
	}

	private final Supplier<int[]> getter;
	private final Consumer<int[]> setter;

	private DynamicIntArray(Supplier<int[]> getter, Consumer<int[]> setter) {
		this.getter = getter;
		this.setter = setter;
	}

	public int capacity() {
		return this.getter.get().length;
	}

	public int length() {
		return this.capacity();
	}

	public int get(int index) {
		return this.getter.get()[index];
	}

	public int set(int index, int value) {
		return this.getter.get()[index] = value;
	}

	public int add(int index, int value) {
		int capacity = index + 1;

		if (this.capacity() < capacity) {
			this.grow(capacity);
		}

		return this.set(index, value);
	}

	public void addAll(DynamicIntArray array) {
		int capacity = array.length();

		if (this.capacity() < capacity) {
			this.grow(capacity);
		}

		System.arraycopy(array.getter.get(), 0, this.getter.get(), 0, array.length());
	}

	public void clear() {
		Arrays.fill(this.getter.get(), 0);
	}

	public void grow(int capacity) {
		this.setter.accept(grow(this.getter.get(), capacity));
	}

	public static int[] grow(int[] array, int capacity) {
		if (capacity > array.length) {
			array = Arrays.copyOf(array, DynamicArrays.newLength(array.length, capacity));
		}

		return array;
	}
}
