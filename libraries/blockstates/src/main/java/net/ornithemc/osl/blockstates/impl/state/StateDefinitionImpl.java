package net.ornithemc.osl.blockstates.impl.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import net.ornithemc.osl.blockstates.api.state.State;
import net.ornithemc.osl.blockstates.api.state.StateDefinition;
import net.ornithemc.osl.blockstates.api.state.property.Property;
import net.ornithemc.osl.blockstates.impl.state.StateDefinitionImpl;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.blockstates.impl.state.StateDefinitionImpl;

public class StateDefinitionImpl<O, S extends State<O, S>> implements StateDefinition<O, S> {

	private final O owner;
	private final Map<String, Property<?>> properties;
	private final List<S> states;

	private StateDefinitionImpl(O owner, Map<String, Property<?>> properties, List<S> states) {
		this.owner = owner;
		this.properties = Collections.unmodifiableMap(new TreeMap<>(properties));
		this.states = Collections.unmodifiableList(new ArrayList<>(states));
	}

	@Override
	public O owner() {
		return this.owner;
	}

	@Override
	public List<S> all() {
		return this.states;
	}

	@Override
	public S any() {
		return this.states.get(0);
	}

	@Override
	public Collection<Property<?>> properties() {
		return this.properties.values();
	}

	@Override
	public Property<?> getProperty(String name) {
		return this.properties.get(name);
	}

	public static class Builder<O, S extends State<O, S>> implements StateDefinition.Builder<O, S> {

		private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");

		private static final Property<?>[] NO_PROPERTIES = new Property<?>[0];
		private static final Comparable<?>[] NO_VALUES = new Comparable<?>[0];
		private static final State<?, ?>[][] NO_NEIGHBORS = new State<?, ?>[0][];

		private final O owner;
		private final Map<String, Property<?>> properties;

		public Builder(O owner) {
			this.owner = owner;
			this.properties = new HashMap<>();
		}

		private <T extends Comparable<T>> Property<T> validateProperty(Property<T> property) {
			String name = property.getName();

			if (!VALID_NAME_PATTERN.matcher(name).matches()) {
				throw new IllegalArgumentException(this.owner + " has invalidly named property: " + name);
			}

			Collection<T> values = property.values();

			if (values.size() <= 1) {
				throw new IllegalArgumentException(this.owner + " attempted use property " + name + " with <= 1 possible values");
			}

			for (T value : values) {
				String valueName = property.getName(value);

				if (!VALID_NAME_PATTERN.matcher(valueName).matches()) {
					throw new IllegalArgumentException(this.owner + " has property: " + name + " with invalidly named value: " + valueName);
				}
			}

			if (this.properties.containsKey(name)) {
				throw new IllegalArgumentException(this.owner + " has duplicate property: " + name);
			}

			return property;
		}

		@Override
		public StateDefinition.Builder<O, S> add(Property<?>... properties) {
			for (Property<?> property : properties) {
				this.properties.put(property.getName(), this.validateProperty(property));
			}

			return this;
		}

		@Override
		public StateDefinition<O, S> build(State.Factory<O, S> factory) {
			return new StateDefinitionImpl<>(this.owner, this.properties, this.buildStates(factory));
		}

		private List<S> buildStates(State.Factory<O, S> factory) {
			if (this.properties.isEmpty()) {
				return buildSingletonState(this.owner, factory);
			} else if (this.properties.size() == 1) {
				return buildStatesForSingleProperty(this.owner, this.properties.values().iterator().next(), factory);
			} else {
				return buildStatesForMultipleProperties(this.owner, this.properties.values().toArray(new Property<?>[this.properties.size()]), factory);
			}
		}

		@SuppressWarnings("unchecked")
		private static <O, S extends State<O, S>> List<S> buildSingletonState(O owner, State.Factory<O, S> factory) {
			return Collections.singletonList(factory.buildState(owner, NO_PROPERTIES, NO_VALUES, (S[][]) NO_NEIGHBORS));
		}

		private static <O, S extends State<O, S>, T extends Comparable<T>> List<S> buildStatesForSingleProperty(O owner, Property<T> property, State.Factory<O, S> factory) {
			Property<?>[] properties = new Property<?>[] { property };
			Collection<T> values = property.values();
			@SuppressWarnings("unchecked")
			S[] neighborsForState = (S[]) new State[values.size()];
			@SuppressWarnings("unchecked")
			S[][] neighbors = (S[][]) new State[][] { neighborsForState };

			List<S> states = new ArrayList<>(values.size());

			for (T value : values) {
				int index = property.indexOf(value);
				S state = factory.buildState(owner, properties, new Comparable<?>[] { value }, neighbors);

				neighborsForState[index] = state;
				states.add(state);
			}

			return states;
		}

		@SuppressWarnings("unchecked")
		private static <O, S extends State<O, S>> List<S> buildStatesForMultipleProperties(O owner, Property<?>[] properties, State.Factory<O, S> factory) {
			List<List<Comparable<?>>> valuesForStates = new ArrayList<>();
			Map<List<Comparable<?>>, S[]> neighborsLookup = new HashMap<>();

			for (Property<?> property : properties) {
				valuesForStates.add((List<Comparable<?>>) property.values());
			}

			valuesForStates = Util.cartesianProduct(valuesForStates);

			for (List<Comparable<?>> valuesForState : valuesForStates) {
				for (int i = 0; i < properties.length; i++) {
					Property<?> property = properties[i];
					List<Comparable<?>> values = new ArrayList<>(property.values().size());
					S[] neighbors = (S[]) new State[property.values().size()];

					values.addAll(valuesForState);
					values.set(i, null);
					neighborsLookup.put(values, neighbors);
				}
			}

			List<S> states = new ArrayList<>(valuesForStates.size());

			for (List<Comparable<?>> valuesForState : valuesForStates) {
				Comparable<?>[] values = valuesForState.toArray(new Comparable<?>[properties.length]);
				S[][] neighbors = (S[][]) new State[properties.length][];

				S state = factory.buildState(owner, properties, values, neighbors);

				for (int i = 0; i < properties.length; i++) {
					Property<?> property = properties[i];
					Comparable<?> value = valuesForState.get(i);
					int index = Property.indexOf(property, value);

					valuesForState.set(i, null);
					neighbors[i] = neighborsLookup.get(valuesForState);
					valuesForState.set(i, value);

					neighbors[i][index] = state;
				}

				states.add(state);
			}

			return states;
		}
	}
}
