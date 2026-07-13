package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.impl.registry.ResourceKeysImpl;

/**
 * A utility class for creating {@linkplain ResourceKey resource keys}.
 */
public final class ResourceKeys {

	/**
	 * Creates a resource key for a registry with the given namespaced identifier.
	 * 
	 * @param <T>        the value type of the registry.
	 * @param identifier the identifier that uniquely identifies the registry.
	 * @return the resource key.
	 */
	public static <T> ResourceKey<T> from(NamespacedIdentifier identifier) {
		return ResourceKeysImpl.from(identifier);
	}

	/**
	 * Creates a resource key for a registry value with the given namespaced identifier.
	 * 
	 * @param <T>        the value type of the registry.
	 * @param registry   the resource key that uniquely identifies the registry.
	 * @param identifier the namespaced identifier that uniquely identifies the value.
	 * @return the resource key.
	 */
	public static <T> ResourceKey<T> from(ResourceKey<? extends Registry<T>> registry, NamespacedIdentifier identifier) {
		return ResourceKeysImpl.from(registry.identifier(), identifier);
	}

	/**
	 * Creates a resource key for a registry value with the given namespaced identifier.
	 * 
	 * @param <T>        the value type of the registry.
	 * @param registry   the namespaced identifier that uniquely identifies the registry.
	 * @param identifier the namespaced identifier that uniquely identifies the value.
	 * @return the resource key.
	 */
	public static <T> ResourceKey<T> from(NamespacedIdentifier registry, NamespacedIdentifier identifier) {
		return ResourceKeysImpl.from(registry, identifier);
	}
}
