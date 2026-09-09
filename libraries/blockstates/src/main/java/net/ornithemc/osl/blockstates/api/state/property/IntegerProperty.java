package net.ornithemc.osl.blockstates.api.state.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntegerProperty extends AbstractProperty<Integer> {

	public static IntegerProperty of(String name, int min, int max) {
		return new IntegerProperty(name, min, max);
	}

	private final int min;
	private final int max;
	private final List<Integer> values;

	private IntegerProperty(String name, int min, int max) {
		super(name, Integer.class);

		if (min < 0) {
			throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
		}
		if (max <= min) {
			throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
		}

		List<Integer> values = new ArrayList<>(max - min + 1);
		for (int value = min; value <= max; value++) {
			values.add(value);
		}

		this.min = min;
		this.max = max;
		this.values = Collections.unmodifiableList(values);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof EnumProperty)) {
			return false;
		}

		IntegerProperty that = (IntegerProperty) o;
		return super.equals(o) && this.values.equals(that.values);
	}

	@Override
	public int hashCode() {
		return 31 * super.hashCode() + this.values.hashCode();
	}

	@Override
	public List<Integer> values() {
		return this.values;
	}

	@Override
	public String getName(Integer value) {
		return value.toString();
	}

	@Override
	public int indexOf(Integer value) {
		return value >= this.min && value <= this.max ? value - this.min : -1;
	}
}
