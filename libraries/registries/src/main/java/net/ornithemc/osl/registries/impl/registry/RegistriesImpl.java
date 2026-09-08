package net.ornithemc.osl.registries.impl.registry;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.RegistryEvents;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;
import net.ornithemc.osl.registries.api.registry.WritableRegistry;

public final class RegistriesImpl {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Registries");

	private static final WritableRegistry<WritableRegistry<?>> WRITABLE_REGISTRY = new SimpleRegistry<>(NamespacedIdentifiers.from("root"));
	private static final Map<NamespacedIdentifier, Registry.Bootstrap> BOOTSTRAPS = new LinkedHashMap<>();

	public static final Registry<? extends Registry<?>> REGISTRY = WRITABLE_REGISTRY;

	public static <T> WritableRegistry<T> get(ResourceKey<? extends Registry<? extends T>> key) {
		@SuppressWarnings("unchecked")
		ResourceKey<WritableRegistry<?>> registryKey = (ResourceKey<WritableRegistry<?>>) (Object) key;
		@SuppressWarnings("unchecked")
		WritableRegistry<T> registry = (WritableRegistry<T>) WRITABLE_REGISTRY.get(registryKey);

		return registry;
	}

	public static <T> SimpleRegistry<T> registerSimple(ResourceKey<? extends Registry<T>> key, Registry.Bootstrap bootstrap) {
		return register(key, new SimpleRegistry<>(key.identifier()), bootstrap);
	}

	public static <T> DefaultedSimpleRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> key, NamespacedIdentifier defaultIdentifier, Registry.Bootstrap bootstrap) {
		return register(key, new DefaultedSimpleRegistry<>(key.identifier(), defaultIdentifier), bootstrap);
	}

	public static <T, R extends WritableRegistry<T>> R register(ResourceKey<? extends Registry<T>> key, R registry, Registry.Bootstrap bootstrap) {
		if (!NamespacedIdentifiers.equals(key.identifier(), registry.identifier())) {
			throw new IllegalArgumentException("illegal key " + key + " for registry " + registry.identifier());
		}

		@SuppressWarnings("unchecked")
		ResourceKey<WritableRegistry<?>> registryKey = (ResourceKey<WritableRegistry<?>>) (Object) key;
		NamespacedIdentifier identifier = registryKey.identifier();

		WRITABLE_REGISTRY.register(registryKey, registry);
		BOOTSTRAPS.put(identifier, bootstrap);

		if (registry != null) {
			LOGGER.debug("Added {} {}", registry.getClass().getSimpleName(), key.identifier());
		}

		return registry;
	}

	public static <T, V extends T> V registerMapping(Registry<T> registry, ResourceKey<T> key, V value) {
		return ((WritableRegistry<T>) registry).register(key, value);
	}

	public static <T, V extends T> V registerMapping(Registry<T> registry, NamespacedIdentifier identifier, V value) {
		return registerMapping(registry, ResourceKeys.from(registry.identifier(), identifier), value);
	}

	@Deprecated
	public static <T, V extends T> V registerMapping(Registry<T> registry, int id, ResourceKey<T> key, V value) {
		value = ((WritableRegistry<T>) registry).register(id, key, value);

		if (value != null) {
			LOGGER.debug("[{}] registered {} {}", id, key.identifier());
		}

		return value;
	}

	@Deprecated
	public static <T, V extends T> V registerMapping(Registry<T> registry, int id, NamespacedIdentifier identifier, V value) {
		return registerMapping(registry, id, ResourceKeys.from(registry.identifier(), identifier), value);
	}

	public static void init() {
		RegistryEvents.BOOTSTRAP_REGISTRIES.invoker().run();

		bootstrap();
		freeze();
		validate();

		LOGGER.info("Bootstrapped {} registries.", BOOTSTRAPS.size());
	}

	private static void bootstrap() {
		BOOTSTRAPS.forEach((identifier, bootstrap) -> {
			try {
				bootstrap.init();
			} catch (Exception e) {
				throw new IllegalStateException("error while bootstrapping registry " + identifier, e);
			}
		});
	}

	private static void freeze() {
		WRITABLE_REGISTRY.freeze();

		for (Registry<?> registry : WRITABLE_REGISTRY) {
			registry.freeze();
		}
	}

	private static void validate() {
		if (REGISTRY.keySet().isEmpty()) {
			throw new IllegalStateException("Unable to load registries!");
		}

		for (Registry<?> registry : REGISTRY) {
			if (registry.keySet().isEmpty()) {
				throw new IllegalStateException("Registry " + registry.identifier() + " is empty after bootstrap!");
			}
		}
	}
}
