package net.ornithemc.osl.blocks.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.block.Block;

import net.ornithemc.osl.blockstates.api.block.Blocks;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public final class VanillaBlocks {

	/**
	 * Namespaced IDs were introduced in 1.7. Before then, the numerical IDs
	 * were the only unique identifiers for blocks. Here we assign namespaced
	 * IDs to pre-1.7 blocks, matching the 1.7 IDs  where possible.
	 * <p>
	 * Note that in 1.7 snapshots the IDs went through several changes.
	 * The IDs used here match those used in Release 1.7.2.
	 */
	private static final String[] IDENTIFIERS = {
		// grouped per 10 for easier lookup

		"air",
		"stone",
		"grass",
		"dirt",
		"cobblestone",
		"planks",
		"sapling",
		"bedrock",
		"flowing_water",
		"water",

		"flowing_lava",
		"lava",
		"sand",
		"gravel",
		"gold_ore",
		"iron_ore",
		"coal_ore",
		"log",
		"leaves",
		"sponge",

		"glass",
		"lapis_ore",
		"lapis_block",
		"dispenser",
		"sandstone",
		"noteblock",
		"bed",
		"golden_rail",
		"detector_rail",
		"sticky_piston",

		"web",
		"tallgrass",
		"deadbush",
		"piston",
		"piston_head",
		"wool",
		"piston_extension",
		"yellow_flower",
		"red_flower",
		"brown_mushroom",

		"red_mushroom",
		"gold_block",
		"iron_block",
		"double_stone_slab",
		"stone_slab",
		"brick_block",
		"tnt",
		"bookshelf",
		"mossy_cobblestone",
		"obsidian",

		"torch",
		"fire",
		"mob_spawner",
		"oak_stairs",
		"chest",
		"redstone_wire",
		"diamond_ore",
		"diamond_block",
		"crafting_table",
		"wheat",

		"farmland",
		"furnace",
		"lit_furnace",
		"standing_sign",
		"wooden_door",
		"ladder",
		"rail",
		"stone_stairs",
		"wall_sign",
		"lever",

		"stone_pressure_plate",
		"iron_door",
		"wooden_pressure_plate",
		"redstone_ore",
		"lit_redstone_ore",
		"unlit_redstone_torch",
		"redstone_torch",
		"stone_button",
		"snow_layer",
		"ice",

		"snow",
		"cactus",
		"clay",
		"reeds",
		"jukebox",
		"fence",
		"pumpkin",
		"netherrack",
		"soul_sand",
		"glowstone",

		"portal",
		"lit_pumpkin",
		"cake",
		"unpowered_repeater",
		"powered_repeater",
		"april_fools_locked_chest",
		"trapdoor",
		"monster_egg",
		"stonebrick",
		"brown_mushroom_block",

		"red_mushroom_block",
		"iron_bars",
		"glass_pane",
		"melon_block",
		"pumpkin_stem",
		"melon_stem",
		"vine",
		"fence_gate",
		"brick_stairs",
		"stone_brick_stairs",

		"mycelium",
		"waterlily",
		"nether_brick",
		"nether_brick_fence",
		"nether_brick_stairs",
		"nether_wart",
		"enchanting_table",
		"brewing_stand",
		"cauldron",
		"end_portal",

		"end_portal_frame",
		"end_stone",
		"dragon_egg",
		"redstone_lamp",
		"lit_redstone_lamp",
		"double_wooden_slab",
		"wooden_slab",
		"cocoa",
		"sandstone_stairs",
		"emerald_ore",

		"ender_chest",
		"tripwire_hook",
		"tripwire",
		"emerald_block",
		"spruce_stairs",
		"birch_stairs",
		"jungle_stairs",
		"command_block",
		"beacon",
		"cobblestone_wall",

		"flower_pot",
		"carrots",
		"potatoes",
		"wooden_button",
		"skull",
		"anvil",
		"trapped_chest",
		"light_weighted_pressure_plate",
		"heavey_weighted_pressure_plate",
		"unpowered_comparator",

		"powered_comparator",
		"daylight_detector",
		"redstone_block",
		"quartz_ore",
		"hopper",
		"quartz_block",
		"quartz_stairs",
		"activator_rail",
		"dropper",
		"stained_hardened_clay",

//		IDs 160 to 169 were unused
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

		"hay",
		"carpet",
		"hardened_clay",
		"coal_block"
	};

	public static final int MAX_ID = 255;

	static void init() {
		// Air block added by Block States API
		register(Blocks.AIR);

		for (Field f : Block.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers()) && Block.class.isAssignableFrom(f.getType())) {
				try {
					Block block = (Block) f.get(null);

					if (block != null) {
						register(block);
					}
				} catch (Throwable t) {
				}
			}
		}
	}

	private static void register(Block block) {
		if (block.id >= 0 && block.id < IDENTIFIERS.length) {
			String identifier = IDENTIFIERS[block.id];

			if (identifier != null) {
				BlockRegistryImpl.register(NamespacedIdentifiers.from(identifier), block);
			}
		}
	}
}
