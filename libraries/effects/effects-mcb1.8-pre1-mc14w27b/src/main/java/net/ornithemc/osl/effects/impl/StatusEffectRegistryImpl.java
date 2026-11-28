package net.ornithemc.osl.effects.impl;

import java.util.Set;

import net.minecraft.entity.living.effect.StatusEffect;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.effects.api.StatusEffectEvents;
import net.ornithemc.osl.effects.impl.effect.StatusEffectIdFixer;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public final class StatusEffectRegistryImpl {

	public static final Registry<StatusEffect> REGISTRY = Registries.registerSimple(RegistryKeys.STATUS_EFFECT, () -> StatusEffect.BY_ID.getClass());

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

	@SuppressWarnings("deprecation")
	public static <T extends StatusEffect> T register(NamespacedIdentifier identifier, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, effect.id, identifier, effect);
		}
	}

	@SuppressWarnings("deprecation")
	public static <T extends StatusEffect> T register(ResourceKey<StatusEffect> key, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			return Registry.register(REGISTRY, effect.id, key, effect);
		}
	}

	@Deprecated
	public static <T extends StatusEffect> T register(int id, NamespacedIdentifier key, T effect) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (effect.id != id) {
				throw new IllegalArgumentException("ID " + id + " does not match effect ID " + effect.id + " for " + key);
			}

			return Registry.register(REGISTRY, id, key, effect);
		}
	}

	public static void init() {
		SyncedRegistries.register(RegistryKeys.STATUS_EFFECT);
		SyncedRegistries.registerFixer(RegistryKeys.STATUS_EFFECT, NamespacedIdentifiers.from("effect/id"), new StatusEffectIdFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerStatusEffects() {
		VanillaStatusEffects.init();
		StatusEffectEvents.REGISTER_STATUS_EFFECTS.invoker().run();
	}

	public static void registerUnknownStatusEffects() {
		for (StatusEffect effect : StatusEffect.BY_ID) {
			if (effect != null && REGISTRY.getIdentifier(effect) == null) {
				NamespacedIdentifier identifier = NamespacedIdentifiers.from("osl", "mob_effect_" + effect.id);

				RegistriesImpl.LOGGER.warn("StatusEffect {}/{}", effect.id, effect.getClass().getSimpleName() + " was not registered to OSL's StatusEffect registry! Adding it as '" + identifier + "'...");
				StatusEffectRegistryImpl.register(identifier, effect);
			}
		}
	}
}
