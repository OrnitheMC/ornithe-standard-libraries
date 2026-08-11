package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

/**
 * Utility class with public access to the root registry and methods for
 * creating and registering registries.
 * 
 * @see Registry
 */
public final class Registries {

	/**
	 * The root registry that holds references to all registered registries.
	 */
	public static final Registry<? extends Registry<?>> REGISTRY = RegistriesImpl.REGISTRY;

	/**
	 * @param <T> the value type of the registry.
	 * @param key the resource key that identifies the registry.
	 * @return the registry identified by the given resource key.
	 */
	public static <T> Registry<T> get(ResourceKey<? extends Registry<? extends T>> key) {
		return RegistriesImpl.get(key);
	}

	/**
	 * @param <T>       the value type of the registry.
	 * @param key       the resource key that identifies the registry.
	 * @param bootstrap the callback for bootstrapping the registry.
	 * @return the registry.
	 * 
	 * @see Registry
	 * @see Registry.Bootstrap
	 */
	public static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> key, Registry.Bootstrap bootstrap) {
		return RegistriesImpl.registerSimple(key, bootstrap);
	}

	/**
	 * @param <T>               the value type of the registry.
	 * @param key               the resource key that identifies the registry.
	 * @param defaultIdentifier the namespaced identifier of the default value of the registry.
	 * @param bootstrap         the callback for bootstrapping the registry.
	 * @return the registry.
	 * 
	 * @see Registry
	 * @see Registry.Bootstrap
	 * @see DefaultedRegistry
	 */
	public static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> key, NamespacedIdentifier defaultIdentifier, Registry.Bootstrap bootstrap) {
		return RegistriesImpl.registerDefaulted(key, defaultIdentifier, bootstrap);
	}
}
