package net.ornithemc.osl.registries.impl;

import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

public final class Bootstrap {

	private static boolean initialized;

	public static void init() {
		if (!initialized) {
			initialized = true;

			RegistriesImpl.init();
			SyncedRegistriesImpl.init();
		}
	}
}
