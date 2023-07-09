package net.ornithemc.osl.core.api.util.function;

import java.io.IOException;

public interface IOConsumer<T> {

	void accept(T t) throws IOException;

}
