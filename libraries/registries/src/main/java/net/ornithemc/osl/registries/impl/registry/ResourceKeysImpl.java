package net.ornithemc.osl.registries.impl.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public final class ResourceKeysImpl {

	private static final Map<Key, ResourceKeyImpl<?>> RESOURCE_KEYS = new HashMap<>();

	public static <T> ResourceKeyImpl<T> from(NamespacedIdentifier identifier) {
		return from(RegistriesImpl.REGISTRY.identifier(), identifier);
	}

	@SuppressWarnings("unchecked")
	public static <T> ResourceKeyImpl<T> from(NamespacedIdentifier registry, NamespacedIdentifier identifier) {
		return (ResourceKeyImpl<T>) RESOURCE_KEYS.computeIfAbsent(new Key(registry, identifier), key -> new ResourceKeyImpl<>(registry, identifier));
	}

	private static final class Key {

		private final NamespacedIdentifier registry;
		private final NamespacedIdentifier identifier;

		private Key(NamespacedIdentifier registry, NamespacedIdentifier identifier) {
			this.registry = registry;
			this.identifier = identifier;
		}

		@Override
		public boolean equals(Object o) {
			return this.registry.equals(((Key) o).registry) && this.identifier.equals(((Key) o).identifier);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.registry, this.identifier);
		}
	}
}
