package net.ornithemc.osl.registries.impl.registry;

import java.util.function.Supplier;

import net.minecraft.util.registry.DefaultedIdRegistry;
import net.minecraft.util.registry.IdRegistry;

import net.ornithemc.osl.registries.api.registry.DefaultedRegistry;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

public final class VanillaRegistries {

	public static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> key, net.minecraft.util.registry.Registry<String, T> registry) {
		return registerSimple(key, registry, () -> null);
	}

	public static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> key, net.minecraft.util.registry.Registry<String, T> registry, Supplier<?> bootstrap) {
		return RegistriesImpl.register(key, WrappedIdRegistry.of(key, (IdRegistry<T>) registry), bootstrap::get);
	}

	public static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> key, net.minecraft.util.registry.Registry<String, T> registry) {
		return registerDefaulted(key, registry, () -> null);
	}

	public static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> key, net.minecraft.util.registry.Registry<String, T> registry, Supplier<?> bootstrap) {
		return RegistriesImpl.register(key, WrappedDefaultedIdRegistry.of(key, (DefaultedIdRegistry<T>) registry), bootstrap::get);
	}
}
