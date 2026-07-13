package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface DefaultedRegistry<T> extends Registry<T> {

	NamespacedIdentifier getDefaultIdentifier();

}
