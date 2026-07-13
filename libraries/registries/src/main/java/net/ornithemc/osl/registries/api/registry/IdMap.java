package net.ornithemc.osl.registries.api.registry;

import java.util.Iterator;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

/**
 * A simple bidirectional map for assigning numerical IDs to objects. IDs can be
 * assigned manually through {@linkplain #put(T, int)} or automatically through
 * {@link #put(T)}. The latter method will assign an ID one higher than the
 * largest ID already in use.
 * 
 * <p>
 * IDs are java {@code int}s and cannot be negative. ID uniqueness is guaranteed
 * but value uniqueness is not. For this reason, the {@link #getId(T)} may
 * return any of the IDs mapped to the given value.
 * 
 * @param <T> the value type.
 */
public final class IdMap<T> implements Iterable<T> {

	private final Int2ReferenceMap<T> values;
	private final Reference2IntMap<T> ids;

	private int nextId = 0;

	public IdMap() {
		this.values = new Int2ReferenceOpenHashMap<>();
		this.ids = new Reference2IntOpenHashMap<>();

		this.ids.defaultReturnValue(-1);
	}

	/**
	 * Adds the given value to the map with the next available ID. This ID is one
	 * higher than the largest ID already in use.
	 * 
	 * @param value the value to add to the map.
	 * @return the value.
	 */
	public T put(T value) {
		return this.put(value, this.nextId);
	}

	/**
	 * Adds the given value to the map with the given ID.
	 * 
	 * @param value the value to add to the map.
	 * @param id    the ID to assign to the value.
	 * @return the value
	 */
	public T put(T value, int id) {
		if (id < 0) {
			throw new IllegalArgumentException("invalid ID " + id + " (must be >= 0)");
		}

		this.values.put(id, value);
		this.ids.put(value, id);

		this.nextId = Math.max(this.nextId, id + 1);

		return value;
	}

	/**
	 * Returns the ID assigned to the given value. Since value uniqueness is not
	 * guaranteed, the given value could appear multiple times, and this method may
	 * return any one of the IDs assigned to it.
	 * 
	 * @return the ID assigned to the given value.
	 */
	public int getId(T value) {
		return this.ids.getInt(value);
	}

	/**
	 * @return the value assigned to the given ID.
	 */
	public T get(int id) {
		return this.values.get(id);
	}

	/**
	 * @return whether this map contains the given value.
	 */
	public boolean has(T value) {
		return this.ids.containsKey(value);
	}

	@Override
	public Iterator<T> iterator() {
		return this.values.values().iterator();
	}

	/**
	 * Clears all values and IDs from this map.
	 */
	public void clear() {
		this.values.clear();
		this.ids.clear();

		this.nextId = 0;
	}
}
