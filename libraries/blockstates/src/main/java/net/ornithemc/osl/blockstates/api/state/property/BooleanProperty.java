package net.ornithemc.osl.blockstates.api.state.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BooleanProperty extends AbstractProperty<Boolean> {

	public static BooleanProperty of(String name) {
		return new BooleanProperty(name);
	}

	private final List<Boolean> values;

	private BooleanProperty(String name) {
		super(name, Boolean.class);

		List<Boolean> values = new ArrayList<>(2);
		values.add(Boolean.TRUE);
		values.add(Boolean.FALSE);

		this.values = Collections.unmodifiableList(values);
	}

	@Override
	public List<Boolean> values() {
		return this.values;
	}

	@Override
	public String getName(Boolean value) {
		return value.toString();
	}

	@Override
	public int indexOf(Boolean value) {
		return value ? 0 : 1;
	}
}
