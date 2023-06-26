package net.ornithemc.osl.core.api.registry;

public final class RegistryKey {

	static final RegistryKey ROOT = new RegistryKey("osl:registries");

	public static RegistryKey of(String value) {
		return of(ROOT, value);
	}

	public static RegistryKey of(RegistryKey parent, String value) {
		return new RegistryKey(parent.value + ":" + value);
	}

	private final String value;

	private RegistryKey(String value) {
		this.value = value;
	}

	public String get() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RegistryKey)) {
			return false;
		}

		return value.equals(((RegistryKey)o).value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
