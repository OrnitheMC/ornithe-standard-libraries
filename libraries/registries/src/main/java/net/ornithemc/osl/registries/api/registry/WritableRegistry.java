package net.ornithemc.osl.registries.api.registry;

public interface WritableRegistry<T> extends Registry<T> {

	<V extends T> V register(ResourceKey<T> key, V value);

	@Deprecated
	<V extends T> V register(int id, ResourceKey<T> key, V value);

}
