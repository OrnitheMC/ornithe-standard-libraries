package net.ornithemc.osl.registries.api.registry;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public interface Registry<T> extends Iterable<T> {

	NamespacedIdentifier identifier();

	T get(int id);

	T get(ResourceKey<T> key);

	T get(NamespacedIdentifier identifier);

	boolean has(T value);

	int getId(T value);

	ResourceKey<T> getKey(T value);

	NamespacedIdentifier getIdentifier(T value);

	Set<ResourceKey<T>> keySet();

	Set<NamespacedIdentifier> identifierSet();

	Registry<T> freeze();

	static <T, V extends T> V register(Registry<T> registry, NamespacedIdentifier identifier, V value) {
		return RegistriesImpl.registerMapping(registry, identifier, value);
	}

	static <T, V extends T> V register(Registry<T> registry, ResourceKey<T> key, V value) {
		return RegistriesImpl.registerMapping(registry, key, value);
	}

	@Deprecated
	static <T, V extends T> V register(Registry<T> registry, int id, NamespacedIdentifier identifier, V value) {
		return RegistriesImpl.registerMapping(registry, id, identifier, value);
	}

	@Deprecated
	static <T, V extends T> V register(Registry<T> registry, int id, ResourceKey<T> key, V value) {
		return RegistriesImpl.registerMapping(registry, id, key, value);
	}

	interface Bootstrap {

		void init();

	}
}
