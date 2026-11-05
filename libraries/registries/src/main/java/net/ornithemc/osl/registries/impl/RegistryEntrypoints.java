package net.ornithemc.osl.registries.impl;

import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.registries.api.RegistryInitializer;

public final class RegistryEntrypoints {

	public static void invoke() {
		FabricLoader.getInstance().invokeEntrypoints(
			RegistryInitializer.ENTRYPOINT_KEY,
			RegistryInitializer.class,
			RegistryInitializer::initRegistries
		);
	}
}
