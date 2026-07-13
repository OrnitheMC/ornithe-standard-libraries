package net.ornithemc.osl.registries.api.registry;

/**
 * A writable registry allows new values to be registered to it.
 * 
 * @param <T> the value type.
 * @see Registry
 */
public interface WritableRegistry<T> extends Registry<T> {

	/**
	 * Registers the given value to the registry.
	 * 
	 * @param <V>      the value type of the value to be registered.
	 * @param key      the resource key that uniquely identifies the value.
	 * @param value    the value to be registered.
	 * @return the registered value.
	 */
	<V extends T> V register(ResourceKey<T> key, V value);

	/**
	 * Registers the given value to the registry.
	 * 
	 * @param <V>      the value type of the value to be registered.
	 * @param id       the ID that uniquely identifies the value.
	 * @param key      the resource key that uniquely identifies the value.
	 * @param value    the value to be registered.
	 * @return the registered value.
	 * 
	 * @deprecated use {@link #register(ResourceKey, Object)} instead.
	 */
	@Deprecated
	<V extends T> V register(int id, ResourceKey<T> key, V value);

}
