package net.ornithemc.osl.registries.impl.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.sync.IdFixer;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMapper;

public final class SyncedRegistriesImpl {

	public static final Registry<SyncedRegistry> SYNCED_REGISTRIES = new SimpleRegistry<>(NamespacedIdentifiers.from("synced_registry"));

	public static void register(ResourceKey<? extends Registry<?>> registry) {
		NamespacedIdentifier identifier = registry.identifier();
		Registry<?> instance = Registries.get(registry);

		if (instance == null) {
			throw new IllegalArgumentException("unknown registry " + identifier);
		} else if (!(instance instanceof ClearableRegistry)) {
			throw new IllegalArgumentException("unsyncable registry " + identifier);
		} else {
			SyncedRegistry synced = SYNCED_REGISTRIES.get(identifier);

			if (synced != null) {
				throw new IllegalArgumentException("duplicate synced registry " + identifier);
			} else {
				Registry.register(SYNCED_REGISTRIES, registry.identifier(), synced = new SyncedRegistry(instance));
			}

			synced.registerMapper(identifier, RegistryMapper.of(instance));
		}
	}

	public static void registerMapper(ResourceKey<? extends Registry<?>> registry, NamespacedIdentifier identifier, IdMapper mapper) {
		SyncedRegistry synced = SYNCED_REGISTRIES.get(registry.identifier());

		if (synced == null) {
			throw new IllegalArgumentException("registry " + registry.identifier() + " is not synced!");
		} else {
			synced.registerMapper(identifier, mapper);
		}
	}

	public static void registerFixer(ResourceKey<? extends Registry<?>> registry, NamespacedIdentifier identifier, IdFixer fixer) {
		SyncedRegistry synced = SYNCED_REGISTRIES.get(registry.identifier());

		if (synced == null) {
			throw new IllegalArgumentException("registry " + registry.identifier() + " is not synced!");
		} else {
			synced.registerFixer(identifier, fixer);
		}
	}

	public static void init() {
		for (Registry<?> registry : Registries.REGISTRY) {
			SyncedRegistry synced = SYNCED_REGISTRIES.get(registry.identifier());

			if (synced != null) {
				synced.getMappings().reset();
			} else {
				RegistriesImpl.LOGGER.debug("registry {} is not synced!", registry.identifier());
			}
		}
	}

	public static void resetMappings() {
		for (SyncedRegistry registry : SYNCED_REGISTRIES) {
			registry.getMappings().reset();
		}
	}

	public static void applyMappings() {
		for (SyncedRegistry registry : SYNCED_REGISTRIES) {
			RegistryMappings mappings = registry.getMappings();

			for (IdMapper mapper : registry.getMappers()) {
				mapper.apply(mappings);
			}
			for (IdFixer fixer : registry.getFixers()) {
				fixer.apply();
			}
		}
	}

	public static void undoMappings() {
		for (SyncedRegistry registry : SYNCED_REGISTRIES) {
			RegistryMappings mappings = registry.getMappings();

			for (IdMapper mapper : registry.getMappers()) {
				mapper.undo(mappings);
			}
			for (IdFixer fixer : registry.getFixers()) {
				fixer.apply();
			}
		}
	}
}
