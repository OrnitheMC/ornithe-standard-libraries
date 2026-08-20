package net.ornithemc.osl.registries.impl.registry.sync;

public final class DynamicArrays {

	// rather than increasing the array length to exactly the requested capacity
	// increase it by 50% of the current length to avoid allocating a lot of arrays 
	public static int newLength(int length, int capacity) {
		while (length < capacity) {
			length += length / 2;
		}

		return length;
	}
}
