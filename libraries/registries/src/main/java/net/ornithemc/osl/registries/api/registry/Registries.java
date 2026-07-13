package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public final class Registries {

	public static final Registry<? extends Registry<?>> REGISTRY = RegistriesImpl.REGISTRY;

	public static <T> Registry<T> get(ResourceKey<? extends Registry<? extends T>> key) {
		return RegistriesImpl.get(key);
	}

	public static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> key, Registry.Bootstrap bootstrap) {
		return RegistriesImpl.registerSimple(key, bootstrap);
	}

	public static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> key, NamespacedIdentifier defaultIdentifier, Registry.Bootstrap bootstrap) {
		return RegistriesImpl.registerDefaulted(key, defaultIdentifier, bootstrap);
	}
}
