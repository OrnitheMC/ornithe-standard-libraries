package net.ornithemc.osl.registries.api.registry.sync;

import net.minecraft.util.Id2ObjectBiMap;

public class Id2ObjectBiMapMapper implements IdMapper {

	public static Id2ObjectBiMapMapper of(Id2ObjectBiMap<?> registry) {
		return new Id2ObjectBiMapMapper(registry);
	}

	private final Id2ObjectBiMap<Object> registry;
	private final Id2ObjectBiMap<Object> backup;

	private boolean applied;

	@SuppressWarnings("unchecked")
	private Id2ObjectBiMapMapper(Id2ObjectBiMap<?> registry) {
		this.registry = (Id2ObjectBiMap<Object>) registry;
		this.backup = new Id2ObjectBiMap<>(registry.size());
	}

	/*
	 * unlike Registry, Id2ObjectBiMap allows duplicate values
	 * thus iterating over all values will not give you all id-value pairs!
	 */

	@Override
	public void apply(RegistryMappings mappings) {
		this.backup.osl$registries$clear();

		for (int id : this.registry.osl$registries$idSet()) {
			this.backup.put(this.registry.get(id), id);
		}

		this.registry.osl$registries$clear();

		for (int oldId : this.backup.osl$registries$idSet()) {
			Object value = this.backup.get(oldId);
			int newId = mappings.remap(oldId);

			if (newId >= 0) {
				this.registry.put(value, newId);
			}
		}

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.registry.osl$registries$clear();

			for (int id : this.backup.osl$registries$idSet()) {
				this.registry.put(this.backup.get(id), id);
			}
		}

		this.applied = false;
	}
}
