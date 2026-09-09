package net.ornithemc.osl.blockstates.api.state.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnumProperty<T extends Enum<T>> extends AbstractProperty<T> {

	public static <T extends Enum<T> & StringSerializable> EnumProperty<T> of(String name, Class<T> type) {
		return of(name, type, value -> true);
	}

	public static <T extends Enum<T> & StringSerializable> EnumProperty<T> of(String name, Class<T> type, Predicate<T> filter) {
		return of(name, type, Arrays.asList(type.getEnumConstants()), filter);
	}

	@SafeVarargs
	public static <T extends Enum<T> & StringSerializable> EnumProperty<T> of(String name, Class<T> type, T... values) {
		return of(name, type, Arrays.asList(values));
	}

	public static <T extends Enum<T> & StringSerializable> EnumProperty<T> of(String name, Class<T> type, Collection<T> values) {
		return of(name, type, values, value -> true);
	}

	private static <T extends Enum<T> & StringSerializable> EnumProperty<T> of(String name, Class<T> type, Collection<T> values, Predicate<T> filter) {
		values = values.stream()
			.filter(filter)
			.collect(Collectors.toList());

		return new EnumProperty<>(name, type, StringSerializable::serializeName, values);
	}

	private final Function<T, String> serializer;
	private final List<T> values;
	private final Map<String, T> valuesByName;
	private final int[] valueIndices;

	protected EnumProperty(String name, Class<T> type, Collection<T> values) {
		this(name, type, T::toString, values);
	}

	protected EnumProperty(String name, Class<T> type, Function<T, String> serializer, Collection<T> values) {
		super(name, type);

		this.serializer = serializer;
		this.values = Collections.unmodifiableList(new ArrayList<>(values));
		this.valuesByName = new HashMap<>();
		this.valueIndices = new int[values.size()];

		for (T value : values) {
			String valueName = serializer.apply(value);
			if (this.valuesByName.containsKey(valueName)) {
				throw new IllegalArgumentException("Multiple values have the same name '" + valueName + "'");
			}

			this.valuesByName.put(valueName, value);
		}
		for (T value : type.getEnumConstants()) {
			this.valueIndices[value.ordinal()] = this.values.indexOf(value);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof EnumProperty)) {
			return false;
		}

		@SuppressWarnings("rawtypes")
		EnumProperty that = (EnumProperty) o;
		return super.equals(o) && this.values.equals(that.values);
	}

	@Override
	public int hashCode() {
		return 31 * super.hashCode() + this.values.hashCode();
	}

	@Override
	public List<T> values() {
		return this.values;
	}

	@Override
	public String getName(T value) {
		return this.serializer.apply(value);
	}

	@Override
	public int indexOf(T value) {
		return this.valueIndices[value.ordinal()];
	}
}
