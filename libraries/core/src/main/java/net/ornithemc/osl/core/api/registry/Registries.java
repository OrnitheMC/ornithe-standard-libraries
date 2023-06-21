package net.ornithemc.osl.core.api.registry;

public final class Registries {

	private static final Registry<RegistryKey, Registry<?, ?>> ROOT = new Registry<>(RegistryKey.ROOT);

	public static <K, V> Registry<K, V> register(RegistryKey name) {
		return registerMapping(ROOT, name, new Registry<>(name));
	}

	public static <K, V> V registerMapping(Registry<? super K, ? super V> registry, K key, V value) {
		return registry.register(key, value);
	}
}
