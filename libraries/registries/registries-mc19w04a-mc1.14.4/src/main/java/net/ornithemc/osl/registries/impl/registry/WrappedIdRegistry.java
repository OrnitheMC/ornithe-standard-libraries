package net.ornithemc.osl.registries.impl.registry;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.IdRegistry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;

public class WrappedIdRegistry<T> implements ClearableRegistry<T> {

	public static <T> WrappedIdRegistry<T> of(ResourceKey<? extends Registry<T>> key, IdRegistry<T> registry) {
		return new WrappedIdRegistry<>(key.identifier(), registry);
	}

	final NamespacedIdentifier identifier;
	final IdRegistry<T> registry;

	private int nextId;
	private boolean frozen;

	WrappedIdRegistry(NamespacedIdentifier identifier, IdRegistry<T> registry) {
		this.identifier = identifier;
		this.registry = registry;

		this.registry.osl$registries$setCallback(this::valueRegistered);
	}

	NamespacedIdentifier serializeKey(Identifier key) {
		return key;
	}

	Identifier deserializeKey(NamespacedIdentifier identifier) {
		return identifier == null ? null : (identifier instanceof Identifier ? (Identifier) identifier : new Identifier(identifier.namespace(), identifier.identifier()));
	}

	ResourceKey<T> resourceKey(NamespacedIdentifier identifier) {
		return identifier == null ? null : ResourceKeys.from(this.identifier, identifier);
	}

	private void valueRegistered(int id, Identifier key, Object value) {
		if (this.frozen) {
			throw new IllegalStateException("registry is frozen!");
		}

		this.nextId = Math.max(this.nextId, id + 1);
	}

	@Override
	public <V extends T> V register(ResourceKey<T> key, V value) {
		return this.register(this.nextId, key, value);
	}

	@Override
	public <V extends T> V register(int id, ResourceKey<T> key, V value) {
		this.registry.m_26252208(id, this.deserializeKey(key.identifier()), value);
		return value;
	}

	@Override
	public NamespacedIdentifier identifier() {
		return this.identifier;
	}

	@Override
	public T get(int id) {
		return this.registry.get(id);
	}

	@Override
	public T get(ResourceKey<T> key) {
		Identifier k = this.deserializeKey(key.identifier());
		return this.registry.containsKey(k) ? this.registry.getOrDefault(k) : null;
	}

	@Override
	public T get(NamespacedIdentifier identifier) {
		Identifier k = this.deserializeKey(identifier);
		return this.registry.containsKey(k) ? this.registry.getOrDefault(k) : null;
	}

	@Override
	public boolean has(T value) {
		return this.registry.osl$registries$has(value);
	}

	@Override
	public int getId(T value) {
		return this.registry.getId(value);
	}

	@Override
	public ResourceKey<T> getKey(T value) {
		return this.resourceKey(this.serializeKey(this.registry.getKey(value)));
	}

	@Override
	public NamespacedIdentifier getIdentifier(T value) {
		return this.serializeKey(this.registry.getKey(value));
	}

	@Override
	public Set<ResourceKey<T>> keySet() {
		return this.registry.keySet().stream().map(this::serializeKey).map(this::resourceKey).collect(Collectors.toSet());
	}

	@Override
	public Set<NamespacedIdentifier> identifierSet() {
		return this.registry.keySet().stream().map(this::serializeKey).collect(Collectors.toSet());
	}

	@Override
	public Iterator<T> iterator() {
		return this.registry.iterator();
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
		this.registry.osl$registries$clear();

		this.nextId = 0;
		this.frozen = false;
	}
}
