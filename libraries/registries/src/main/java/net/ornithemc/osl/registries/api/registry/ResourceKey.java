package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * A resource key uniquely identifies a registered object.
 * 
 * @param <T> the value type of the registry.
 * @see Registry
 */
public interface ResourceKey<T> {

	/**
	 * @return the {@linkplain NamespacedIdentifier identifier} that uniquely identifies the registry.
	 */
	NamespacedIdentifier registry();

	/**
	 * @return the {@linkplain NamespacedIdentifier identifier} that uniquely identifies value within the registry.
	 */
	NamespacedIdentifier identifier();

}
