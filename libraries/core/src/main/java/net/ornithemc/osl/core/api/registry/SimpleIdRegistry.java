package net.ornithemc.osl.core.api.registry;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * @deprecated use Registries API instead
 */
@Deprecated
public class SimpleIdRegistry<T> implements Iterable<T> {

	private final Int2ReferenceMap<T> values;
	private final Reference2IntMap<T> ids;
	private final Map<NamespacedIdentifier, T> registry;
	private final Map<T, NamespacedIdentifier> keys;

	private int nextId = 0;

	public SimpleIdRegistry() {
		this.values = new Int2ReferenceOpenHashMap<>();
		this.ids = new Reference2IntOpenHashMap<>();
		this.registry = new HashMap<>();
		this.keys = new IdentityHashMap<>();

		this.ids.defaultReturnValue(-1);
	}

	public T register(NamespacedIdentifier key, T value) {
		return this.register(this.nextId++, key, value);
	}

	public T register(int id, NamespacedIdentifier key, T value) {
		if (id < 0) {
			throw new IllegalStateException("invalid ID " + id + " (must be >= 0)");
		}
		if (this.values.containsKey(id)) {
			throw new IllegalStateException("duplicate ID " + id + " (" + this.getKey(this.get(id)) + " and " + key + ")");
		}
		if (this.registry.containsKey(key)) {
			throw new IllegalStateException("duplicate Namespaced ID " + key + " (" + this.getId(this.get(key)) + " and " + id + ")");
		}
		if (this.ids.containsKey(value) || this.keys.containsKey(value)) {
			throw new IllegalStateException("value registered twice (" + this.getId(value) + ", " + this.getKey(value) + " and " + id + ", " + key + ")");
		}

		this.values.put(id, value);
		this.ids.put(value, id);
		this.registry.put(key, value);
		this.keys.put(value, key);

		this.nextId = Math.max(this.nextId, id + 1);

		return value;
	}

	public T get(int id) {
		return this.values.get(id);
	}

	public T get(NamespacedIdentifier key) {
		return this.registry.get(key);
	}

	public boolean has(T value) {
		return this.ids.containsKey(value);
	}

	public int getId(T value) {
		return this.ids.getInt(value);
	}

	public NamespacedIdentifier getKey(T value) {
		return this.keys.get(value);
	}

	public Set<NamespacedIdentifier> keySet() {
		return this.registry.keySet();
	}

	@Override
	public Iterator<T> iterator() {
		return this.registry.values().iterator();
	}
}
