package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface ResourceKey<T> {

	NamespacedIdentifier registry();

	NamespacedIdentifier identifier();

}
