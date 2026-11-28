package net.ornithemc.osl.enchantments.api;

import java.util.Set;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.enchantments.impl.EnchantmentRegistryImpl;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

/**
 * Public access to the Enchantments registry.
 */
public final class EnchantmentRegistry {

	public static final Registry<Enchantment> REGISTRY = EnchantmentRegistryImpl.REGISTRY;

	/**
	 * @return the numerical ID assigned to the given enchantment.
	 */
	public static int getId(Enchantment enchantment) {
		return EnchantmentRegistryImpl.getId(enchantment);
	}

	/**
	 * @return the namespaced ID assigned to the given enchantment.
	 */
	public static NamespacedIdentifier getIdentifier(Enchantment enchantment) {
		return EnchantmentRegistryImpl.getIdentifier(enchantment);
	}

	/**
	 * @return the resource key assigned to the given enchantment.
	 */
	public static ResourceKey<Enchantment> getKey(Enchantment enchantment) {
		return EnchantmentRegistryImpl.getKey(enchantment);
	}

	/**
	 * @return the enchantment mapped to the given numerical ID.
	 */
	public static Enchantment getEnchantment(int id) {
		return EnchantmentRegistryImpl.getEnchantment(id);
	}

	/**
	 * @return the enchantment mapped to the given namespaced ID.
	 */
	public static Enchantment getEnchantment(NamespacedIdentifier identifier) {
		return EnchantmentRegistryImpl.getEnchantment(identifier);
	}

	/**
	 * @return the enchantment mapped to the given resource key.
	 */
	public static Enchantment getEnchantment(ResourceKey<Enchantment> key) {
		return EnchantmentRegistryImpl.getEnchantment(key);
	}

	/**
	 * @return a set containing all namespaced IDs in the registry.
	 */
	public static Set<NamespacedIdentifier> identifierSet() {
		return EnchantmentRegistryImpl.identifierSet();
	}

	/**
	 * @return a set containing all resource keys in the registry.
	 */
	public static Set<ResourceKey<Enchantment>> keySet() {
		return EnchantmentRegistryImpl.keySet();
	}

	/**
	 * @param <T>         the enchantment type.
	 * @param identifier  the namespaced ID of the enchantment.
	 * @param enchantment the enchantment to register.
	 * @return the registered enchantment.
	 */
	public static <T extends Enchantment> T register(NamespacedIdentifier identifier, T enchantment) {
		return EnchantmentRegistryImpl.register(identifier, enchantment);
	}

	/**
	 * @param <T>         the enchantment type.
	 * @param key         the resource key of the enchantment.
	 * @param enchantment the enchantment to register.
	 * @return the registered enchantment.
	 */
	public static <T extends Enchantment> T register(ResourceKey<Enchantment> key, T enchantment) {
		return EnchantmentRegistryImpl.register(key, enchantment);
	}
}
