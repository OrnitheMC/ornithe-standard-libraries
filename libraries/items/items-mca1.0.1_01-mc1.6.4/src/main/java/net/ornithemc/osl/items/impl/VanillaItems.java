package net.ornithemc.osl.items.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

final class VanillaItems {

	/**
	 * Namespaced IDs were introduced in 1.7. Before then, the numerical IDs
	 * were the only unique identifiers for items. Here we assign namespaced
	 * IDs to pre-1.7 items, matching the 1.7 IDs  where possible.
	 * <p>
	 * Note that in 1.7 snapshots the IDs went through several changes.
	 * The IDs used here match those used in Release 1.7.2.
	 */
	private static final String[] IDENTIFIERS = {
		// grouped per 10 for easier lookup

		"iron_shovel",
		"iron_pickaxe",
		"iron_axe",
		"flint_and_steel",
		"apple",
		"bow",
		"arrow",
		"coal",
		"diamond",
		"iron_ingot",

		"gold_ingot",
		"iron_sword",
		"wooden_sword",
		"wooden_shovel",
		"wooden_pickaxe",
		"wooden_axe",
		"stone_sword",
		"stone_shovel",
		"stone_pickaxe",
		"stone_axe",

		"diamond_sword",
		"diamond_shovel",
		"diamond_pickaxe",
		"diamond_axe",
		"stick",
		"bowl",
		"mushroom_stew",
		"golden_sword",
		"golden_shovel",
		"golden_pickaxe",

		"golden_axe",
		"string",
		"feather",
		"gunpowder",
		"wooden_hoe",
		"stone_hoe",
		"iron_hoe",
		"diamond_hoe",
		"golden_hoe",
		"wheat_seeds",

		"wheat",
		"bread",
		"leather_helmet",
		"leather_chestplate",
		"leather_leggings",
		"leather_boots",
		"chainmail_helmet",
		"chainmail_chestplate",
		"chainmail_leggings",
		"chainmail_boots",

		"iron_helmet",
		"iron_chestplate",
		"iron_leggings",
		"iron_boots",
		"diamond_helmet",
		"diamond_chestplate",
		"diamond_leggings",
		"diamond_boots",
		"golden_helmet",
		"golden_chestplate",

		"golden_leggings",
		"golden_boots",
		"flint",
		"porkchop",
		"cooked_porkchop",
		"painting",
		"golden_apple",
		"sign",
		"wooden_door",
		"bucket",

		"water_bucket",
		"lava_bucket",
		"minecart",
		"saddle",
		"iron_door",
		"redstone",
		"snowball",
		"boat",
		"leather",
		"milk_bucket",

		"brick",
		"clay_ball",
		"reeds",
		"paper",
		"book",
		"slime_ball",
		"chest_minecart",
		"furnace_minecart",
		"egg",
		"compass",

		"fishing_rod",
		"clock",
		"glowstone_dust",
		"fish",
		"cooked_fished",
		"dye",
		"bone",
		"sugar",
		"cake",
		"bed",

		"repeater",
		"cookie",
		"filled_map",
		"shears",
		"melon",
		"pumpkin_seeds",
		"melon_seeds",
		"beef",
		"cooked_beef",
		"chicken",

		"cooked_chicken",
		"rotten_flesh",
		"ender_pearl",
		"blaze_rod",
		"ghast_tear",
		"gold_nugget",
		"nether_wart",
		"potion",
		"glass_bottle",
		"spider_eye",

		"fermented_spider_eye",
		"blaze_powder",
		"magma_cream",
		"brewing_stand",
		"cauldron",
		"ender_eye",
		"speckled_melon",
		"spawn_egg",
		"experience_bottle",
		"fire_charge",

		"writable_book",
		"written_book",
		"emerald",
		"item_frame",
		"flower_pot",
		"carrot",
		"potato",
		"baked_potato",
		"poisonous_potato",
		"map",

		"golden_carrot",
		"skull",
		"carrot_on_a_stick",
		"nether_star",
		"pumpkin_pie",
		"fireworks",
		"firework_charge",
		"enchanted_book",
		"comparator",
		"netherbrick",

		"quartz",
		"tnt_minecart",
		"hopper_minecart",
		null,
		null,
		null,
		null,
		null,
		null,
		null,

		null,
		"iron_horse_armor",
		"golden_horse_armor",
		"diamond_horse_armor",
		"lead",
		"name_tag",
		"command_block_minecart"
	};
	private static final String[] DISC_IDENTIFIERS = {
		"record_13",
		"record_cat",
		"record_blocks",
		"record_chirp",
		"record_far",
		"record_mall",
		"record_mellohi",
		"record_stal",
		"record_strad",
		"record_ward",

		"record_11",
		"record_wait"
	};

	static final int ITEM_ID_OFFSET = 256;
	static final int DISC_ITEM_ID_OFFSET = 2000 + ITEM_ID_OFFSET;

	static void init() {
		for (Field f : Item.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers()) && Item.class.isAssignableFrom(f.getType())) {
				try {
					Item item = (Item) f.get(null);

					if (item != null) {
						String identifier = null;

						if (item.id >= DISC_ITEM_ID_OFFSET) {
							int id = item.id - DISC_ITEM_ID_OFFSET;

							if (id >= 0 && id < DISC_IDENTIFIERS.length) {
								identifier = DISC_IDENTIFIERS[id];
							}
						} else if (item.id >= ITEM_ID_OFFSET) {
							int id = item.id - ITEM_ID_OFFSET;

							if (id >= 0 && id < IDENTIFIERS.length) {
								identifier = IDENTIFIERS[id];
							}
						}

						if (identifier != null) {
							ItemRegistryImpl.register(item.id, NamespacedIdentifiers.from(identifier), item);
						}
					}
				} catch (Throwable t) {
				}
			}
		}
	}
}
