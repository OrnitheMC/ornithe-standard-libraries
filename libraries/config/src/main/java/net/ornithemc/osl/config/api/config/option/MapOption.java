package net.ornithemc.osl.config.api.config.option;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;

import net.ornithemc.osl.config.api.config.option.validator.OptionValidators;

/**
 * This class provides a {@linkplain ModifiableOption} implementation for the
 * {@linkplain java.util.Map Map} type. This class is generic, thus you can
 * parameterize it as you would a {@code Map}. The key and value types must be
 * provided when creating a {@code MapOption} instance - it is needed for
 * serialization.
 * <p>
 * To fulfill the contract established in {@code ModifiableOption}, this class
 * provides its own {@code add} and {@code remove} methods. Modifying the map
 * by calling methods on the {@code Map} itself will throw an exception, as
 * {@link #get} returns an unmodifiable {@code Map}.
 * <p>
 * For convenience, option serializers for this class are built into this API.
 * However, because this class is generic, you must register object serializers
 * for the generic type parameters instead.
 */
public class MapOption<K, V> extends ModifiableOption<Map<K, V>> {

	protected final Class<K> keyType;
	protected final Class<V> valueType;
	protected final BiPredicate<K, V> entryValidator;

	public MapOption(Class<K> keyType, Class<V> valueType, String name, String description) {
		this(keyType, valueType, name, description, Collections.emptyMap());
	}

	public MapOption(Class<K> keyType, Class<V> valueType, String name, String description, Map<K, V> defaultValue) {
		super(name, description, defaultValue);

		this.keyType = keyType;
		this.valueType = valueType;
		this.entryValidator = (key, value) -> key != null && value != null;
	}

	public MapOption(Class<K> keyType, Class<V> valueType, String name, String description, Map<K, V> defaultValue, BiPredicate<K, V> entryValidator) {
		super(name, description, defaultValue, OptionValidators.map(entryValidator));

		this.keyType = keyType;
		this.valueType = valueType;
		this.entryValidator = entryValidator;
	}

	public Class<K> getKeyType() {
		return keyType;
	}

	public Class<V> getValueType() {
		return valueType;
	}

	@Override
	protected Map<K, V> unmodifiable(Map<K, V> value) {
		return Collections.unmodifiableMap(value);
	}

	@Override
	protected Map<K, V> modifiable(Map<K, V> value) {
		return new LinkedHashMap<>(value);
	}

	public boolean put(K key, V value) {
		int size = get().size();

		modify(map -> {
			if (entryValidator.test(key, value)) {
				map.put(key, value);
			}
		});

		return get().size() != size;
	}

	public boolean remove(Object key) {
		int size = get().size();

		modify(map -> {
			map.remove(key);
		});

		return get().size() != size;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		modify(map -> {
			Map<? extends K, ? extends V> entries = new LinkedHashMap<>(m);
			entries.entrySet().removeIf(e -> !entryValidator.test(e.getKey(), e.getValue()));
			map.putAll(entries);
		});
	}

	public void clear() {
		modify(Map::clear);
	}
}
