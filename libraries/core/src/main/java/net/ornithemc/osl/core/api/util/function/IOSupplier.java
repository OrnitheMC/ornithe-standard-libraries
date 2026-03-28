package net.ornithemc.osl.core.api.util.function;

import java.io.IOException;

public interface IOSupplier<T> {

	T get() throws IOException;

}
