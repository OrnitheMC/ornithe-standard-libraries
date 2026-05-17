package net.ornithemc.osl.blockstates.api.state;

import java.util.Collection;

import net.ornithemc.osl.blockstates.api.state.property.Property;

public interface State<O, S extends State<O, S>> {

	boolean is(O o);

	Collection<Property<?>> properties();

	<T extends Comparable<T>> boolean has(Property<T> property);

	<T extends Comparable<T>> T get(Property<T> property);

	<T extends Comparable<T>, V extends T> S set(Property<T> property, V value);

	<T extends Comparable<T>> S cycle(Property<T> property);

	interface Factory<O, S extends State<O, S>> {

		S buildState(O owner, Property<?>[] properties, Comparable<?>[] values, State<O, S>[][] neighbors);

	}
}
