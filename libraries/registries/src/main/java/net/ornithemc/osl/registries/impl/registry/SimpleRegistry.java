package net.ornithemc.osl.registries.impl.registry;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.WritableRegistry;

public class SimpleRegistry<T> implements WritableRegistry<T>, ClearableRegistry<T> {

	private final NamespacedIdentifier identifier;

	private final Int2ReferenceMap<T> values;
	private final Reference2IntMap<T> ids;
	private final Map<NamespacedIdentifier, T> registry;
	private final Map<T, ResourceKey<T>> keys;
	private final Map<T, NamespacedIdentifier> identifiers;

	private int nextId = 0;
	private boolean frozen;

	public SimpleRegistry(NamespacedIdentifier identifier) {
		this.identifier = identifier;

		this.values = new Int2ReferenceOpenHashMap<>();
		this.ids = new Reference2IntOpenHashMap<>();
		this.registry = new HashMap<>();
		this.keys = new IdentityHashMap<>();
		this.identifiers = new IdentityHashMap<>();

		this.ids.defaultReturnValue(-1);
	}

	@Override
	public <V extends T> V register(ResourceKey<T> key, V value) {
		return this.register(this.nextId++, key, value);
	}

	@Override
	public <V extends T> V register(int id, ResourceKey<T> key, V value) {
		if (this.frozen) {
			throw new IllegalStateException("registry is frozen!");
		}
		if (id < 0) {
			throw new IllegalStateException("invalid ID " + id + " (must be >= 0)");
		}
		if (this.values.containsKey(id)) {
			throw new IllegalStateException("duplicate ID " + id + " (" + this.getKey(this.get(id)) + " and " + key + ")");
		}
		if (this.registry.containsKey(key.identifier())) {
			throw new IllegalStateException("duplicate Namespaced ID " + key.identifier() + " (" + this.getId(this.get(key)) + " and " + id + ")");
		}
		if (this.ids.containsKey(value) || this.keys.containsKey(value) || this.identifiers.containsKey(value)) {
			throw new IllegalStateException("value registered twice (" + this.getId(value) + ", " + this.getKey(value) + " and " + id + ", " + key + ")");
		}

		this.values.put(id, value);
		this.ids.put(value, id);
		this.registry.put(key.identifier(), value);
		this.keys.put(value, key);
		this.identifiers.put(value, key.identifier());

		this.nextId = Math.max(this.nextId, id + 1);

		return value;
	}

	@Override
	public NamespacedIdentifier identifier() {
		return this.identifier;
	}

	@Override
	public T get(int id) {
		return this.values.get(id);
	}

	@Override
	public T get(ResourceKey<T> key) {
		return this.registry.get(key.identifier());
	}

	@Override
	public T get(NamespacedIdentifier identifier) {
		return this.registry.get(identifier);
	}

	@Override
	public int getId(T value) {
		return this.ids.getInt(value);
	}

	@Override
	public ResourceKey<T> getKey(T value) {
		return this.keys.get(value);
	}

	@Override
	public NamespacedIdentifier getIdentifier(T value) {
		return this.identifiers.get(value);
	}

	@Override
	public Set<ResourceKey<T>> keySet() {
		return this.keys.values().stream().collect(Collectors.toSet());
	}

	@Override
	public Set<NamespacedIdentifier> identifierSet() {
		return this.registry.keySet();
	}

	@Override
	public Iterator<T> iterator() {
		return this.registry.values().iterator();
	}

	@Override
	public Registry<T> freeze() {
		if (!this.frozen) {
			this.frozen = true;
		}

		return this;
	}

	@Override
	public void clear() {
		this.values.clear();
		this.ids.clear();
		this.registry.clear();
		this.keys.clear();
		this.identifiers.clear();

		this.nextId = 0;
		this.frozen = false;
	}
}
