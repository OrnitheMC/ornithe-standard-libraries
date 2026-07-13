package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.impl.registry.ResourceKeysImpl;

public final class ResourceKeys {

	public static <T> ResourceKey<T> from(NamespacedIdentifier identifier) {
		return ResourceKeysImpl.from(identifier);
	}

	public static <T> ResourceKey<T> from(ResourceKey<? extends Registry<T>> registry, NamespacedIdentifier identifier) {
		return ResourceKeysImpl.from(registry.identifier(), identifier);
	}

	public static <T> ResourceKey<T> from(NamespacedIdentifier registry, NamespacedIdentifier identifier) {
		return ResourceKeysImpl.from(registry, identifier);
	}
}
