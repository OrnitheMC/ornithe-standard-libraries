package net.ornithemc.osl.blockstates.impl.state;

import java.util.Arrays;
import java.util.Collection;

import net.ornithemc.osl.blockstates.api.state.State;
import net.ornithemc.osl.blockstates.api.state.property.Property;

public abstract class AbstractState<O, S extends State<O, S>> implements State<O, S> {

	protected final O owner;

	private final Property<?>[] properties;
	private final Comparable<?>[] values;
	private final S[][] neighbors;

	@SuppressWarnings("unchecked")
	protected AbstractState(O owner, Property<?>[] properties, Comparable<?>[] values, State<O, S>[][] neighbors) {
		this.owner = owner;
		this.properties = properties;
		this.values = values;
		this.neighbors = (S[][]) neighbors;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.owner);
		if (this.properties.length > 0) {
			sb.append('[');
			for (int i = 0; i < this.properties.length; i++) {
				Property<?> property = this.properties[i];
				Comparable<?> value = this.values[i];

				if (i > 0) {
					sb.append(',');
				}
				sb.append(property.getName());
				sb.append('=');
				sb.append(Property.getName(property, value));
			}
			sb.append(']');
		}

		return sb.toString();
	}

	@Override
	public Collection<Property<?>> properties() {
		return Arrays.asList(this.properties);
	}

	private int indexOf(Property<?> property) {
		for (int i = 0; i < this.properties.length; i++) {
			if (property == this.properties[i]) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public <T extends Comparable<T>> boolean has(Property<T> property) {
		return this.indexOf(property) != -1;
	}

	@Override
	public <T extends Comparable<T>> T get(Property<T> property) {
		int index = this.indexOf(property);

		if (index == -1) {
			throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.owner);
		} else {
			return property.getType().cast(this.values[index]);
		}
	}

	@Override
	public <T extends Comparable<T>, V extends T> S set(Property<T> property, V value) {
		int index = this.indexOf(property);

		if (index == -1) {
			throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.owner);
		}

		int valueIndex = property.indexOf(value);

		if (valueIndex == -1) {
			throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on " + this.owner + ", it is not an allowed value");
		}

		return this.neighbors[index][valueIndex];
	}

	@Override
	public <T extends Comparable<T>> S cycle(Property<T> property) {
		int index = this.indexOf(property);

		if (index == -1) {
			throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.owner);
		}

		int valueIndex = property.indexOf(this.get(property)) + 1;

		if (valueIndex >= this.neighbors[index].length) {
			valueIndex = 0;
		}

		return this.neighbors[index][valueIndex];
	}
}
