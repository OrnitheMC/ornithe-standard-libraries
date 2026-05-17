package net.ornithemc.osl.blockstates.api.state.property;

public abstract class AbstractProperty<T extends Comparable<T>> implements Property<T> {

	private final Class<T> type;
	private final String name;

	protected AbstractProperty(String name, Class<T> type) {
		this.type = type;
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (this.getClass() != o.getClass()) {
			return false;
		}

		@SuppressWarnings("rawtypes")
		AbstractProperty that = (AbstractProperty) o;
		return this.type.equals(that.type) && this.name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return 31 * this.type.hashCode() + this.name.hashCode();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[name: " + this.name + ", type: " + this.type + ", values: " + this.values() + "]";
	}

	@Override
	public Class<T> getType() {
		return this.type;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
