package net.ornithemc.osl.registries.api.registry;

import java.util.Iterator;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

public final class IdMap<T> implements Iterable<T> {

	private final Int2ReferenceMap<T> values;
	private final Reference2IntMap<T> ids;

	private int nextId = 0;

	public IdMap() {
		this.values = new Int2ReferenceOpenHashMap<>();
		this.ids = new Reference2IntOpenHashMap<>();

		this.ids.defaultReturnValue(-1);
	}

	public T put(T value) {
		return this.put(this.nextId, value);
	}

	public T put(int id, T value) {
		if (id < 0) {
			throw new IllegalStateException("invalid ID " + id + " (must be >= 0)");
		}

		this.values.put(id, value);
		this.ids.put(value, id);

		this.nextId = Math.max(this.nextId, id + 1);

		return value;
	}

	public T get(int id) {
		return this.values.get(id);
	}

	public boolean has(T value) {
		return this.ids.containsKey(value);
	}

	public int getId(T value) {
		return this.ids.getInt(value);
	}

	@Override
	public Iterator<T> iterator() {
		return this.values.values().iterator();
	}

	public void clear() {
		this.values.clear();
		this.ids.clear();

		this.nextId = 0;
	}
}
