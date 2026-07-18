package net.ornithemc.osl.registries.impl.access;

import net.minecraft.resource.Identifier;

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

	interface RegisterCallback {

		void valueRegistered(int id, Identifier key, Object value);

	}
}
