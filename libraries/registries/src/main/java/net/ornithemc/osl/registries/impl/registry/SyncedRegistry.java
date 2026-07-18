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
		}
	}

	public void registerFixer(NamespacedIdentifier identifier, IdFixer fixer) {
		if (this.fixers.containsKey(identifier)) {
			throw new IllegalArgumentException("duplicate ID fixer " + identifier + " for registry " + this.registry.identifier());
		} else {
			this.fixers.put(identifier, fixer);
		}
	}

	public void init() {
		this.mappings.reset();
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
