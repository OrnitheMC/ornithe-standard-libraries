package net.ornithemc.osl.registries.impl.registry;

import net.ornithemc.osl.registries.api.registry.WritableRegistry;

public interface ClearableRegistry<T> extends WritableRegistry<T> {

	void clear();

}
