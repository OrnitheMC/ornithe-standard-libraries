package net.ornithemc.osl.registries.impl.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.DefaultedRegistry;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

public class DefaultedSimpleRegistry<T> extends SimpleRegistry<T> implements DefaultedRegistry<T> {

	private final NamespacedIdentifier defaultIdentifier;

	private T defaultValue;

	public DefaultedSimpleRegistry(NamespacedIdentifier identifier, NamespacedIdentifier defaultIdentifier) {
		super(identifier);

		this.defaultIdentifier = defaultIdentifier;
	}

	@Override
	public NamespacedIdentifier getDefaultIdentifier() {
		return this.defaultIdentifier;
	}

	@Override
	public <V extends T> V register(int id, ResourceKey<T> key, V value) {
		if (this.defaultIdentifier.equals(key.identifier())) {
			this.defaultValue = value;
		}

		return super.register(id, key, value);
	}

	@Override
	public T get(int id) {
		T value = super.get(id);
		return value == null ? this.defaultValue : value;
	}

	@Override
	public T get(ResourceKey<T> key) {
		T value = super.get(key);
		return value == null ? this.defaultValue : value;
	}

	@Override
	public T get(NamespacedIdentifier identifier) {
		T value = super.get(identifier);
		return value == null ? this.defaultValue : value;
	}

	@Override
	public int getId(T value) {
		int id = super.getId(value);
		return id == -1 ? super.getId(this.defaultValue) : id;
	}

	@Override
	public ResourceKey<T> getKey(T value) {
		ResourceKey<T> key = super.getKey(value);
		return key == null ? super.getKey(this.defaultValue) : key;
	}

	@Override
	public NamespacedIdentifier getIdentifier(T value) {
		NamespacedIdentifier identifier = super.getIdentifier(value);
		return identifier == null ? this.defaultIdentifier : identifier;
	}

	@Override
	public Registry<T> freeze() {
		if (this.defaultValue == null) {
			throw new IllegalStateException("Attempted to freeze defaulted registry " + this.identifier() + " before default value was registered!");
		}

		return super.freeze();
	}

	@Override
	public void clear() {
		super.clear();

		this.defaultValue = null;
	}
}
