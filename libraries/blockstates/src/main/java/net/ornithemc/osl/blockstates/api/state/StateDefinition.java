package net.ornithemc.osl.blockstates.api.state;

import java.util.Collection;
import java.util.List;

import net.ornithemc.osl.blockstates.api.state.property.Property;

public interface StateDefinition<O, S extends State<O, S>> {

	O owner();

	List<S> all();

	S any();

	Collection<Property<?>> properties();

	Property<?> getProperty(String name);

	interface Builder<O, S extends State<O, S>> {

		Builder<O, S> add(Property<?>... properties);

		StateDefinition<O, S> build(State.Factory<O, S> factory);

	}
}
