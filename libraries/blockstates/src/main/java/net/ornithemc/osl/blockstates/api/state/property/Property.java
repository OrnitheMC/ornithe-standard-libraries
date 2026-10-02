package net.ornithemc.osl.blockstates.api.state.property;

import java.util.List;

public interface Property<T extends Comparable<T>> {

	Class<T> getType();

	String getName();

	List<T> values();

	String getName(T value);

	int indexOf(T value);

	@SuppressWarnings("unchecked")
	static <T extends Comparable<T>> String getName(Property<T> property, Comparable<?> value) {
		return property.getName((T) value);
	}

	@SuppressWarnings("unchecked")
	static <T extends Comparable<T>> int indexOf(Property<T> property, Comparable<?> value) {
		return property.indexOf((T) value);
	}
}
