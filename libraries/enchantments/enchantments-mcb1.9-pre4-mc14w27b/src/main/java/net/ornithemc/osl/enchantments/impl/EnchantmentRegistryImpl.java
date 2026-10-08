package net.ornithemc.osl.enchantments.impl;

import java.util.Set;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.enchantments.api.EnchantmentEvents;
import net.ornithemc.osl.enchantments.impl.enchantment.EnchantmentIdFixer;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public final class EnchantmentRegistryImpl {

	public static final Registry<Enchantment> REGISTRY = Registries.registerSimple(RegistryKeys.ENCHANTMENT, () -> Enchantment.BY_ID.getClass());

	private static boolean locked = true;

	public static int getId(Enchantment enchantment) {
		return REGISTRY.getId(enchantment);
	}

	public static NamespacedIdentifier getIdentifier(Enchantment enchantment) {
		return REGISTRY.getIdentifier(enchantment);
	}

	public static ResourceKey<Enchantment> getKey(Enchantment enchantment) {
		return REGISTRY.getKey(enchantment);
	}

	public static Enchantment getEnchantment(int id) {
		return REGISTRY.get(id);
	}

	public static Enchantment getEnchantment(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Enchantment getEnchantment(ResourceKey<Enchantment> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Enchantment>> keySet() {
		return REGISTRY.keySet();
	}

	@SuppressWarnings("deprecation")
	public static <T extends Enchantment> T register(NamespacedIdentifier identifier, T enchantment) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, enchantment.id, identifier, enchantment);
		}
	}

	@SuppressWarnings("deprecation")
	public static <T extends Enchantment> T register(ResourceKey<Enchantment> key, T enchantment) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, enchantment.id, key, enchantment);
		}
	}

	@Deprecated
	public static <T extends Enchantment> T register(int id, NamespacedIdentifier key, T enchantment) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (enchantment.id != id) {
				throw new IllegalArgumentException("ID " + id + " does not match enchantment ID " + enchantment.id + " for " + key);
			}

			return Registry.register(REGISTRY, id, key, enchantment);
		}
	}

	public static void init() {
		SyncedRegistries.register(RegistryKeys.ENCHANTMENT);
		SyncedRegistries.registerFixer(RegistryKeys.ENCHANTMENT, NamespacedIdentifiers.from("enchantment/id"), new EnchantmentIdFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerEnchantments() {
		VanillaEnchantments.init();
		EnchantmentEvents.REGISTER_ENCHANTMENTS.invoker().run();
	}

	public static void registerUnknownEnchantments() {
		for (Enchantment enchantment : Enchantment.BY_ID) {
			if (enchantment != null && REGISTRY.getIdentifier(enchantment) == null) {
				NamespacedIdentifier identifier = NamespacedIdentifiers.from("osl", "enchantment_" + enchantment.id);

				RegistriesImpl.LOGGER.warn("Enchantment {}/{}", enchantment.id, enchantment.getClass().getSimpleName() + " was not registered to OSL's Enchantment registry! Adding it as '" + identifier + "'...");
				EnchantmentRegistryImpl.register(identifier, enchantment);
			}
		}
	}
}
