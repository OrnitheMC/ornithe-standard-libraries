package net.ornithemc.osl.config.api.config.option;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import net.ornithemc.osl.config.api.config.option.validator.OptionValidators;

/**
 * This class provides a {@linkplain ModifiableOption} implementation for the
 * {@linkplain java.util.Map Map} type. This class is generic, thus you can
 * parameterize it as you would a {@code Map}. The key and value types must be
 * provided when creating a {@code MapOption} instance - it is needed for
 * serialization.
 * <p>
 * This class implements the {@code Map} interface to simplify querying and
 * modifying the value of this option. To fulfill the contract established in
 * {@code ModifiableOption}, the maps returned by this class'
 * {@link #getDefault} and {@link #get} methods return unmodifiable views of the
 * default and current value of this option. Adding and removing elements to the
 * current value should be done by calling methods from the {@code Map}
 * interface instead.
 * <p>
 * For convenience, option serializers for this class are built into this API.
 * However, because this class is generic, you must register object serializers
 * for the generic type parameters instead.
 */
public class MapOption<K, V> extends ModifiableOption<Map<K, V>> implements Map<K, V> {

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

	@Override
	public int size() {
		return get().size();
	}

	@Override
	public boolean isEmpty() {
		return get().isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return get().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return get().containsValue(value);
	}

	@Override
	public V get(Object key) {
		return get().get(key);
	}

	@Override
	public V put(K key, V value) {
		V prev = get(key);

		modify(map -> {
			map.put(key, value);
		});

		return prev;
	}

	@Override
	public V remove(Object key) {
		V prev = get(key);

		modify(map -> {
			map.remove(key);
		});

		return prev;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		modify(map -> {
			map.putAll(m);
		});
	}

	@Override
	public void clear() {
		modify(Map::clear);
	}

	@Override
	public Set<K> keySet() {
		return get().keySet();
	}

	@Override
	public Collection<V> values() {
		return get().values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return get().entrySet();
	}
}
