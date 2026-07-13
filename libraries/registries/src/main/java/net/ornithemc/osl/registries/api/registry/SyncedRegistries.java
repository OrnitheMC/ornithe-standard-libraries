package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.sync.IdFixer;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

public final class SyncedRegistries {

	public static void register(ResourceKey<? extends Registry<?>> registry) {
		SyncedRegistriesImpl.register(registry);
	}

	public static void registerMapper(ResourceKey<? extends Registry<?>> registry, NamespacedIdentifier identifier, IdMapper mapper) {
		SyncedRegistriesImpl.registerMapper(registry, identifier, mapper);
	}

	public static void registerFixer(ResourceKey<? extends Registry<?>> registry, NamespacedIdentifier identifier, IdFixer fixer) {
		SyncedRegistriesImpl.registerFixer(registry, identifier, fixer);
	}
}
