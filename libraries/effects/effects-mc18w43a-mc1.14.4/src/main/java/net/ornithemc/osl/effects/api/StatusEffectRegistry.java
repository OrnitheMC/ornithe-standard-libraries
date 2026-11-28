package net.ornithemc.osl.effects.api;

import java.util.Set;

import net.minecraft.entity.living.effect.StatusEffect;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.effects.impl.StatusEffectRegistryImpl;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

/**
 * Public access to the StatusEffects registry.
 */
public final class StatusEffectRegistry {

	public static final Registry<StatusEffect> REGISTRY = StatusEffectRegistryImpl.REGISTRY;

	/**
	 * @return the numerical ID assigned to the given effect.
	 */
	public static int getId(StatusEffect effect) {
		return StatusEffectRegistryImpl.getId(effect);
	}

	/**
	 * @return the namespaced ID assigned to the given effect.
	 */
	public static NamespacedIdentifier getIdentifier(StatusEffect effect) {
		return StatusEffectRegistryImpl.getIdentifier(effect);
	}

	/**
	 * @return the resource key assigned to the given effect.
	 */
	public static ResourceKey<StatusEffect> getKey(StatusEffect effect) {
		return StatusEffectRegistryImpl.getKey(effect);
	}

	/**
	 * @return the effect mapped to the given numerical ID.
	 */
	public static StatusEffect getStatusEffect(int id) {
		return StatusEffectRegistryImpl.getStatusEffect(id);
	}

	/**
	 * @return the effect mapped to the given namespaced ID.
	 */
	public static StatusEffect getStatusEffect(NamespacedIdentifier identifier) {
		return StatusEffectRegistryImpl.getStatusEffect(identifier);
	}

	/**
	 * @return the effect mapped to the given resource key.
	 */
	public static StatusEffect getStatusEffect(ResourceKey<StatusEffect> key) {
		return StatusEffectRegistryImpl.getStatusEffect(key);
	}

	/**
	 * @return a set containing all namespaced IDs in the registry.
	 */
	public static Set<NamespacedIdentifier> identifierSet() {
		return StatusEffectRegistryImpl.identifierSet();
	}

	/**
	 * @return a set containing all resource keys in the registry.
	 */
	public static Set<ResourceKey<StatusEffect>> keySet() {
		return StatusEffectRegistryImpl.keySet();
	}

	/**
	 * @param <T>        the effect type.
	 * @param identifier the namespaced ID of the effect.
	 * @param effect     the effect to register.
	 * @return the registered effect.
	 */
	public static <T extends StatusEffect> T register(NamespacedIdentifier identifier, T effect) {
		return StatusEffectRegistryImpl.register(identifier, effect);
	}

	/**
	 * @param <T>    the effect type.
	 * @param key    the resource key of the effect.
	 * @param effect the effect to register.
	 * @return the registered effect.
	 */
	public static <T extends StatusEffect> T register(ResourceKey<StatusEffect> key, T effect) {
		return StatusEffectRegistryImpl.register(key, effect);
	}
}
