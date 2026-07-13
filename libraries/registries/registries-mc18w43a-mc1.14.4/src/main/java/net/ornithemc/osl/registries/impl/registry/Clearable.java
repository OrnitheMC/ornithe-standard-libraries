package net.ornithemc.osl.registries.impl.registry;

public interface Clearable {

	static void clear(Object o) {
		if (o instanceof Clearable) {
			((Clearable) o).osl$registries$clear();
		} else {
			throw new IllegalArgumentException("not clearable: " + o);
		}
	}

	default void osl$registries$clear() {
		throw new AbstractMethodError();
	}
}
