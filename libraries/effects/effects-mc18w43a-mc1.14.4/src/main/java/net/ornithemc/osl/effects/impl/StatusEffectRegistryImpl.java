package net.ornithemc.osl.effects.impl;

import java.util.Set;

import net.minecraft.entity.living.effect.StatusEffect;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.effects.api.StatusEffectEvents;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

public final class StatusEffectRegistryImpl {

	public static final Registry<StatusEffect> REGISTRY = VanillaRegistries.registerSimple(RegistryKeys.STATUS_EFFECT, net.minecraft.util.registry.Registry.STATUS_EFFECT);

	private static boolean locked = true;

	public static int getId(StatusEffect effect) {
		return REGISTRY.getId(effect);
	}

	public static NamespacedIdentifier getIdentifier(StatusEffect effect) {
		return REGISTRY.getIdentifier(effect);
	}

	public static ResourceKey<StatusEffect> getKey(StatusEffect effect) {
		return REGISTRY.getKey(effect);
	}

	public static StatusEffect getStatusEffect(int id) {
		return REGISTRY.get(id);
	}

	public static StatusEffect getStatusEffect(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static StatusEffect getStatusEffect(ResourceKey<StatusEffect> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<StatusEffect>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends StatusEffect> T register(NamespacedIdentifier identifier, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, identifier, effect);
		}
	}

	public static <T extends StatusEffect> T register(ResourceKey<StatusEffect> key, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, key, effect);
		}
	}

	public static void init() {
		SyncedRegistries.register(RegistryKeys.STATUS_EFFECT);
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerStatusEffects() {
		StatusEffectEvents.REGISTER_STATUS_EFFECTS.invoker().run();
	}
}
