package net.ornithemc.osl.registries.api.registry.sync;

public interface IdMapper {

	/**
	 * Apply the given ID mappings to the bound registry or map.
	 */
	void apply(RegistryMappings mappings);

	/**
	 * Undo the given ID mappings and reset the bound registry or map.
	 */
	void undo(RegistryMappings mappings);

}
