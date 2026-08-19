package net.ornithemc.osl.registries.impl.registry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.sync.IdFixer;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.impl.registry.sync.SerializableRegistryMappings;

public class SyncedRegistry {

	private final Registry<?> registry;

	private final SerializableRegistryMappings mappings;
	private final Map<NamespacedIdentifier, IdMapper> mappers;
	private final Map<NamespacedIdentifier, IdFixer> fixers;

	public SyncedRegistry(Registry<?> registry) {
		this.registry = registry;

		this.mappings = SerializableRegistryMappings.of(this.registry);
		this.mappers = new LinkedHashMap<>();
		this.fixers = new LinkedHashMap<>();
	}

	public void registerMapper(NamespacedIdentifier identifier, IdMapper mapper) {
		if (this.mappers.containsKey(identifier)) {
			throw new IllegalArgumentException("duplicate ID mapper " + identifier + " for registry " + this.registry.identifier());
		} else {
			this.mappers.put(identifier, mapper);

			if (mapper != null) {
				RegistriesImpl.LOGGER.debug("[{}] added ID mapper {}", this.registry.identifier(), identifier);
			}
		}
	}

	public void registerFixer(NamespacedIdentifier identifier, IdFixer fixer) {
		if (this.fixers.containsKey(identifier)) {
			throw new IllegalArgumentException("duplicate ID fixer " + identifier + " for registry " + this.registry.identifier());
		} else {
			this.fixers.put(identifier, fixer);

			if (fixer != null) {
				RegistriesImpl.LOGGER.debug("[{}] added ID fixer {}", this.registry.identifier(), identifier);
			}
		}
	}

	public void init() {
		this.mappings.reset();
	}

	public void runMappersAndFixers(boolean apply) {
		for (Map.Entry<NamespacedIdentifier, IdMapper> e : this.mappers.entrySet()) {
			IdMapper mapper = e.getValue();

			try {
				if (apply) {
					mapper.apply(this.mappings);
				} else {
					mapper.undo(this.mappings);
				}
			} catch (Throwable t) {
				throw new RuntimeException("error running ID mapper " + this.registry.identifier() + "/" + e.getKey());
			}
		}
		for (Map.Entry<NamespacedIdentifier, IdFixer> e : this.fixers.entrySet()) {
			IdFixer fixer = e.getValue();

			try {
				fixer.apply();
			} catch (Throwable t) {
				throw new RuntimeException("error running ID fixer " + this.registry.identifier() + "/" + e.getKey());
			}
		}
	}

	public NamespacedIdentifier identifier() {
		return this.registry.identifier();
	}

	public SerializableRegistryMappings getMappings() {
		return this.mappings;
	}

	public Collection<IdMapper> getMappers() {
		return  this.mappers.values();
	}

	public Collection<IdFixer> getFixers() {
		return  this.fixers.values();
	}
}
