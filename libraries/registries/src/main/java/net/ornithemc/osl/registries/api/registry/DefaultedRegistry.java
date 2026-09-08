package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * A defaulted registry has a default value. If a value is requested from this
 * registry but no mapping for the given ID, {@linkplain NamespacedIdentifier
 * namespaced identifier} or {@linkplain ResourceKey resource key} is found, the
 * default value is returned.
 * <br> Likewise, if the ID, namespaced identifier, or resource key of a value
 * is requested, but no mapping is found, that of the default value is returned.
 * 
 * @param <T> the value type.
 * @see Registry
 */
public interface DefaultedRegistry<T> extends Registry<T> {

	/**
	 * @return the {@linkplain NamespacedIdentifier} namespaced identifier that
	 *         uniquely identifies the default value.
	 */
	NamespacedIdentifier getDefaultIdentifier();

}
