package net.ornithemc.osl.registries.impl.registry;

import net.minecraft.util.registry.DefaultedIdRegistry;
import net.minecraft.util.registry.IdRegistry;

import net.ornithemc.osl.registries.api.registry.DefaultedRegistry;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

public final class VanillaRegistries {

	public static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> key, net.minecraft.util.registry.Registry<T> registry) {
		return RegistriesImpl.register(key, WrappedIdRegistry.of(key, (IdRegistry<T>) registry), () -> { });
	}

	public static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> key, net.minecraft.util.registry.Registry<T> registry) {
		return RegistriesImpl.register(key, WrappedDefaultedIdRegistry.of(key, (DefaultedIdRegistry<T>) registry), () -> { });
	}
}
