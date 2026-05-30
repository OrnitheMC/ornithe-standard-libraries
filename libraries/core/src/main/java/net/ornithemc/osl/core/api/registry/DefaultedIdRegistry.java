package net.ornithemc.osl.core.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public class DefaultedIdRegistry<T> extends SimpleIdRegistry<T> {

	private final NamespacedIdentifier defaultKey;

	private T defaultValue;

	public DefaultedIdRegistry(String defaultKey) {
		this(NamespacedIdentifiers.from(defaultKey));
	}

	public DefaultedIdRegistry(NamespacedIdentifier defaultKey) {
		this.defaultKey = defaultKey;
	}

	@Override
	public T register(int id, NamespacedIdentifier key, T value) {
		if (this.defaultKey.equals(key)) {
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
	public T get(NamespacedIdentifier key) {
		T value = super.get(key);
		return value == null ? this.defaultValue : value;
	}
}
