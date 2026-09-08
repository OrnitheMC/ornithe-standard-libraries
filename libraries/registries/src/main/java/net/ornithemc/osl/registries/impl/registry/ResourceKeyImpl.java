package net.ornithemc.osl.registries.impl.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

public final class ResourceKeyImpl<T> implements ResourceKey<T> {

	private final NamespacedIdentifier registry;
	private final NamespacedIdentifier identifier;

	ResourceKeyImpl(NamespacedIdentifier registry, NamespacedIdentifier identifier) {
		this.registry = registry;
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return "ResourceKey[" + this.registry + "/" + this.identifier + "]";
	}

	@Override
	public NamespacedIdentifier registry() {
		return this.registry;
	}

	@Override
	public NamespacedIdentifier identifier() {
		return this.identifier;
	}
}
