package net.ornithemc.osl.enchantments.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public final class VanillaEnchantments {

	/**
	 * Namespaced IDs were introduced in 14w28a. Before then, the numerical IDs
	 * were the only unique identifiers for enchantments, and were still used
	 * until 1.9 . Here we assign namespaced IDs to pre-1.9 enchantments, matching
	 * the 1.9 IDs  where possible.
	 */
	private static final String[] IDENTIFIERS = {
		// grouped per 10 for easier lookup

		"protection",
		"fire_protection",
		"feather_falling",
		"blast_protection",
		"projectile_protection",
		"respiration",
		"aqua_affinity",
		"thorns",
		"depth_strider",
		null,

		null,
		null,
		null,
		null,
		null,
		null,
		"sharpness",
		"smite",
		"bane_of_arthropods",
		"knockback",

		"fire_aspect",
		"looting",
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,

		null,
		null,
		"efficiency",
		"silk_touch",
		"unbreaking",
		"fortune",
		null,
		null,
		null,
		null,

		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		"power",
		"punch",

		"flame",
		"infinity",
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,

		null,
		"luck_of_the_sea",
		"lure"
	};

	public static final int MAX_ID = 255;

	static void init() {
		for (Field f : Enchantment.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers()) && Enchantment.class.isAssignableFrom(f.getType())) {
				try {
					Enchantment enchantment = (Enchantment) f.get(null);

					if (enchantment != null) {
						register(enchantment);
					}
				} catch (Throwable t) {
				}
			}
		}
	}

	private static void register(Enchantment enchantment) {
		if (enchantment.id >= 0 && enchantment.id < IDENTIFIERS.length) {
			String identifier = IDENTIFIERS[enchantment.id];

			if (identifier != null) {
				EnchantmentRegistryImpl.register(NamespacedIdentifiers.from(identifier), enchantment);
			}
		}
	}
}
