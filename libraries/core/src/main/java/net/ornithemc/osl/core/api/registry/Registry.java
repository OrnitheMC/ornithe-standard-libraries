package net.ornithemc.osl.core.api.registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class Registry<K, V> {

	final RegistryKey name;
	private final Map<K, V> values;
	private final Map<V, K> keys;

	Registry(RegistryKey name) {
		this.name = name;
		this.values = new LinkedHashMap<>();
		this.keys = new LinkedHashMap<>();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Registry)) {
			return false;
		}

		return name.equals(((Registry<?, ?>)o).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public <T extends V> T register(K key, T value) {
		if (values.containsKey(key)) {
			throw new IllegalStateException("cannot register duplicate key " + key + " to registry " + name);
		}
		if (keys.containsKey(value)) {
			throw new IllegalStateException("cannot register duplicate value " + value + " to registry " + name);
		}

		values.put(key, value);
		keys.put(value, key);

		return value;
	}

	public V get(K key) {
		return values.get(key);
	}

	public K getKey(V value) {
		return keys.get(value);
	}
}
