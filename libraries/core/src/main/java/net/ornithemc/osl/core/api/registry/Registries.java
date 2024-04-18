package net.ornithemc.osl.core.api.registry;

public final class Registries {

	private static final Registry<RegistryKey, Registry<?, ?>> ROOT = new Registry<>(RegistryKey.ROOT);

	public static <K, V> Registry<K, V> register(RegistryKey name) {
		return registerMapping(ROOT, name, new Registry<>(name));
	}

	public static <K, V> V registerMapping(Registry<? super K, ? super V> registry, K key, V value) {
		return registry.register(key, value);
	}

	public static <K, V> V getMapping(Registry<? super K, ? super V> registry, K key) {
		return registry.get(key);
	}

	public static <K, V> K getKey(Registry<? super K, ? super V> registry, V value) {
		return registry.getKey(value);
	}
}
