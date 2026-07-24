package net.ornithemc.osl.registries.api.registry.sync;

import java.util.Arrays;

public final class DynamicArrays {

	public static <T> int length(T[] array) {
		for (int index = array.length - 1; index >= 0; index--) {
			if (array[index] != null) {
				return index + 1;
			}
		}

		return 0;
	}

	public static <T> T[] set(T[] array, int index, T value) {
		array = grow(array, index + 1);
		array[index] = value;

		return array;
	}

	public static <T> T[] grow(T[] array, int capacity) {
		if (capacity > array.length) {
			array = Arrays.copyOf(array, newLength(array.length, capacity));
		}

		return array;
	}

	public static boolean[] grow(boolean[] array, int capacity) {
		if (capacity > array.length) {
			array = Arrays.copyOf(array, newLength(array.length, capacity));
		}

		return array;
	}

	public static int[] grow(int[] array, int capacity) {
		if (capacity > array.length) {
			array = Arrays.copyOf(array, newLength(array.length, capacity));
		}

		return array;
	}

	// rather than increasing the array length to exactly the requested capacity
	// increase it by 50% of the current length to avoid allocating a lot of arrays 
	private static int newLength(int length, int capacity) {
		while (length < capacity) {
			length += length / 2;
		}

		return length;
	}
}
