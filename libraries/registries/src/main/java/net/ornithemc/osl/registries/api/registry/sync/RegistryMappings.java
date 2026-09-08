package net.ornithemc.osl.registries.api.registry.sync;

import net.ornithemc.osl.registries.api.registry.ResourceKey;

/**
 * These mappings can be used to map old IDs to new IDs and vice versa.
 */
public interface RegistryMappings {

	/**
	 * @return the new ID for the given resource key.
	 */
	int remap(ResourceKey<?> key);

	/**
	 * @return the new ID for the given resource ID.
	 */
	int remap(int id);

	/**
	 * @return the old ID for the given resource key.
	 */
	int unmap(ResourceKey<?> key);

	/**
	 * @return the old ID for the given resource ID.
	 */
	int unmap(int id);

}
