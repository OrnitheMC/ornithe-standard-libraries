package net.ornithemc.osl.effects.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.entity.living.effect.StatusEffect;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public final class VanillaStatusEffects {

	/**
	 * Namespaced IDs were introduced in 14w28a. Before then, the numerical IDs
	 * were the only unique identifiers for status effects, and were still used
	 * until 1.9 . Here we assign namespaced IDs to pre-1.9 effects, matching
	 * the 1.9 IDs  where possible.
	 */
	private static final String[] IDENTIFIERS = {
		// grouped per 10 for easier lookup

		null,
		"speed",
		"slowness",
		"haste",
		"mining_fatigue",
		"strength",
		"instant_health",
		"instant_damage",
		"jump_boost",
		"nausea",
		
		"regeneration",
		"resistance",
		"fire_resistance",
		"water_breathing",
		"invisibility",
		"blindness",
		"night_vision",
		"hunger",
		"weakness",
		"poison",
		
		"wither",
		"health_boost",
		"absorption",
		"saturation"
	};

	public static final int MAX_ID = 255;

	static void init() {
		for (Field f : StatusEffect.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers()) && StatusEffect.class.isAssignableFrom(f.getType())) {
				try {
					StatusEffect effect = (StatusEffect) f.get(null);

					if (effect != null) {
						register(effect);
					}
				} catch (Throwable t) {
				}
			}
		}
	}

	private static void register(StatusEffect effect) {
		if (effect.id >= 0 && effect.id < IDENTIFIERS.length) {
			String identifier = IDENTIFIERS[effect.id];

			if (identifier != null) {
				StatusEffectRegistryImpl.register(NamespacedIdentifiers.from(identifier), effect);
			}
		}
	}
}
