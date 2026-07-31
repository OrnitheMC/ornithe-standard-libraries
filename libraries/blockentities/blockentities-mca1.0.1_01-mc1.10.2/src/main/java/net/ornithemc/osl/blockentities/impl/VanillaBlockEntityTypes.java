package net.ornithemc.osl.blockentities.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

final class VanillaBlockEntityTypes {

	static final BiMap<String, String> IDENTIFIERS = HashBiMap.create();

	static {
		IDENTIFIERS.put("Furnace", "furnace");
		IDENTIFIERS.put("Chest", "chest");
		IDENTIFIERS.put("EnderChest", "ender_chest");
		IDENTIFIERS.put("RecordPlayer", "jukebox");
		IDENTIFIERS.put("Trap", "dispenser");
		IDENTIFIERS.put("Dropper", "dropper");
		IDENTIFIERS.put("Sign", "sign");
		IDENTIFIERS.put("MobSpawner", "mob_spawner");
		IDENTIFIERS.put("Music", "noteblock");
		IDENTIFIERS.put("Piston", "piston");
		IDENTIFIERS.put("Cauldron", "brewing_stand");
		IDENTIFIERS.put("EnchantTable", "enchanting_table");
		IDENTIFIERS.put("Airportal", "end_portal");
		IDENTIFIERS.put("Control", "command_block");
		IDENTIFIERS.put("Beacon", "beacon");
		IDENTIFIERS.put("Skull", "skull");
		IDENTIFIERS.put("DLDetector", "daylight_detector");
		IDENTIFIERS.put("Hopper", "hopper");
		IDENTIFIERS.put("Comparator", "comparator");
		IDENTIFIERS.put("FlowerPot", "flower_pot");
		IDENTIFIERS.put("Banner", "banner");
		IDENTIFIERS.put("Structure", "structure_block");
		IDENTIFIERS.put("EndGateway", "end_gateway");
	}
}
