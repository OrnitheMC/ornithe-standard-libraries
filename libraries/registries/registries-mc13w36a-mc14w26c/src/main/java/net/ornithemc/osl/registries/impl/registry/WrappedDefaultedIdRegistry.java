package net.ornithemc.osl.registries.impl.registry;

import net.minecraft.util.registry.DefaultedIdRegistry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.DefaultedRegistry;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.impl.mixin.common.DefaultedIdRegistryAccess;

public class WrappedDefaultedIdRegistry<T> extends WrappedIdRegistry<T> implements DefaultedRegistry<T> {

	public static <T> WrappedDefaultedIdRegistry<T> of(ResourceKey<? extends Registry<T>> key, DefaultedIdRegistry<T> registry) {
		return new WrappedDefaultedIdRegistry<>(key.identifier(), registry);
	}

	private WrappedDefaultedIdRegistry(NamespacedIdentifier identifier, DefaultedIdRegistry<T> registry) {
		super(identifier, registry);
	}

	@Override
	public NamespacedIdentifier getDefaultIdentifier() {
		return this.serializeKey(((DefaultedIdRegistryAccess) this.registry).accessDefaultKey());
	}
}
