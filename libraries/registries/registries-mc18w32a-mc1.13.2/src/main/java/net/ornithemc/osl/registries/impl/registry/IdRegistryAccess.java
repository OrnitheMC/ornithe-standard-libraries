package net.ornithemc.osl.registries.impl.registry;

import net.ornithemc.osl.registries.impl.registry.WrappedIdRegistry.RegisterCallback;

public interface IdRegistryAccess {

	default void osl$registries$setCallback(RegisterCallback callback) {
		throw new AbstractMethodError();
	}

	default boolean osl$registries$has(Object value) {
		throw new AbstractMethodError();
	}

	default void osl$registries$clear() {
		throw new AbstractMethodError();
	}
}
