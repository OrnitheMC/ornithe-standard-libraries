package net.ornithemc.osl.registries.api.registry;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

/**
 * Registries are used to keep track of in-game resources. A registry is assumed
 * to hold all known values of a type (e.g. all known blocks), and each value is
 * assigned a unique numerical ID as well as a unique
 * {@linkplain NamespacedIdentifier namespaced identifier}. Because of this,
 * registries play an important role in serialization and, consequently, in
 * server-client communication and world save formats.
 * 
 * <p>
 * Examples of registries are those for blocks, items, entity types, biomes,
 * painting motives, stats, and enchantments. Registries themselves are also
 * registered to a registry (see {@linkplain Registries#REGISTRY}).
 * 
 * @param <T> the value type.
 */
public interface Registry<T> extends Iterable<T> {

	/**
	 * @return the namespaced identifier that uniquely identifies this registry.
	 */
	NamespacedIdentifier identifier();

	/**
	 * @return the value assigned to the given ID.
	 */
	T get(int id);

	/**
	 * @return the value mapped to the given resource key.
	 */
	T get(ResourceKey<T> key);

	/**
	 * @return the value mapped to the given namespaced identifier.
	 */
	T get(NamespacedIdentifier identifier);

	/**
	 * @return the ID mapped to the given value.
	 */
	int getId(T value);

	/**
	 * @return the resource key mapped to the given value.
	 */
	ResourceKey<T> getKey(T value);

	/**
	 * @return the namespaced identifier mapped to the given value.
	 */
	NamespacedIdentifier getIdentifier(T value);

	/**
	 * @return a set containing the resource keys of all values in this registry.
	 */
	Set<ResourceKey<T>> keySet();

	/**
	 * @return a set containing the namespaced identifiers of all values in this registry.
	 */
	Set<NamespacedIdentifier> identifierSet();

	/**
	 * Freezes this registry, disallowing any changes to be written to it.
	 * 
	 * @return this registry.
	 */
	Registry<T> freeze();

	/**
	 * Helper method for registering a mapping to the given registry.
	 * 
	 * @param <T>      the value type of the registry.
	 * @param <V>      the value type of the value to be registered.
	 * @param registry the registry to register the value to.
	 * @param key      the resource key that uniquely identifies the value.
	 * @param value    the value to be registered.
	 * @return the registered value.
	 */
	static <T, V extends T> V register(Registry<T> registry, ResourceKey<T> key, V value) {
		return RegistriesImpl.registerMapping(registry, key, value);
	}

	/**
	 * Helper method for registering a mapping to the given registry.
	 * 
	 * @param <T>        the value type of the registry.
	 * @param <V>        the value type of the value to be registered.
	 * @param registry   the registry to register the value to.
	 * @param identifier the namespaced identifier that uniquely identifies the value.
	 * @param value      the value to be registered.
	 * @return the registered value.
	 */
	static <T, V extends T> V register(Registry<T> registry, NamespacedIdentifier identifier, V value) {
		return RegistriesImpl.registerMapping(registry, identifier, value);
	}

	/**
	 * Helper method for registering a mapping to the given registry.
	 * 
	 * @param <T>      the value type of the registry.
	 * @param <V>      the value type of the value to be registered.
	 * @param registry the registry to register the value to.
	 * @param id       the ID that uniquely identifies the value.
	 * @param key      the resource key that uniquely identifies the value.
	 * @param value    the value to be registered.
	 * @return the registered value.
	 * 
	 * @deprecated use {@link #register(Registry, ResourceKey, Object)} instead.
	 */
	@Deprecated
	static <T, V extends T> V register(Registry<T> registry, int id, ResourceKey<T> key, V value) {
		return RegistriesImpl.registerMapping(registry, id, key, value);
	}

	/**
	 * Helper method for registering a mapping to the given registry.
	 * 
	 * @param <T>        the value type of the registry.
	 * @param <V>        the value type of the value to be registered.
	 * @param registry   the registry to register the value to.
	 * @param id         the ID that uniquely identifies the value.
	 * @param identifier the namespaced identifier that uniquely identifies the value.
	 * @param value      the value to be registered.
	 * @return the registered value.
	 * 
	 * @deprecated use {@link #register(Registry, NamespacedIdentifier, Object)} instead.
	 */
	@Deprecated
	static <T, V extends T> V register(Registry<T> registry, int id, NamespacedIdentifier identifier, V value) {
		return RegistriesImpl.registerMapping(registry, id, identifier, value);
	}

	/**
	 * A registry bootstrap is a callback to populate a registry's contents. It is
	 * called upon game start-up, just before the registries are frozen. That makes
	 * this bootstrap the last chance for values to be registered to the registry.
	 * The registry may be populated earlier than this bootstrap, though, it is
	 * simply a utility. You may bootstrap your registry in any other way at any
	 * other point so long as it's before the registry freeze.
	 * 
	 * <p>
	 * A common pattern for registry bootstrapping is to use a separate class to
	 * hold the registry contents and populate the registry in its static
	 * initializer. Loading that class is then the trigger for bootstrapping the
	 * registry. An example is shown below.
	 * 
	 * <pre>
	 * {@code
	 * public class ExampleRegistries {
	 * 
	 * 	// Note the method reference to CookieRecipes.init()!
	 * 	public static final Registry<CookieRecipe> COOKIE_RECIPE = Registries.registerSimple(REGISTRY_KEY, CookieRecipes::init);
	 * 
	 * }
	 * 
	 * public class CookieRecipes {
	 * 
	 * 	public static final CookieRecipe CHOCOLATE_CHIP = Registry.register(ExampleRegistries.COOKIE_RECIPE, NamespacedIdentifiers.from("example", "chocolate_chip"), new ChocolateChipCookieRecipe());
	 * 
	 * 	public static void init() {
	 * 		// this method is the trigger to load the class
	 * 		// the static initializer handles populating the registry
	 * 	}
	 * }
	 * }
	 * </pre>
	 */
	interface Bootstrap {

		/**
		 * Bootstrap the registry, ensuring all its contents are created and registered.
		 */
		void init();

	}
}
