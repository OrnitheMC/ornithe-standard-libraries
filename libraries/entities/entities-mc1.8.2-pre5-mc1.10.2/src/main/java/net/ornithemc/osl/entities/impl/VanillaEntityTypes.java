package net.ornithemc.osl.entities.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

final class VanillaEntityTypes {

	static final BiMap<String, String> IDENTIFIERS = HashBiMap.create();

	static {
		IDENTIFIERS.put("Item", "item");
		IDENTIFIERS.put("XPOrb", "xp_orb");
		IDENTIFIERS.put("AreaEffectCloud", "area_effect_cloud");
		IDENTIFIERS.put("ThrownEgg", "egg");
		IDENTIFIERS.put("LeashKnot", "leash_knot");
		IDENTIFIERS.put("Painting", "painting");
		IDENTIFIERS.put("Arrow", "arrow");
		IDENTIFIERS.put("Snowball", "snowball");
		IDENTIFIERS.put("Fireball", "fireball");
		IDENTIFIERS.put("SmallFireball", "small_fireball");
		IDENTIFIERS.put("ThrownEnderPearl", "ender_pearl");
		IDENTIFIERS.put("EyeOfEnderSignal", "eye_of_ender_signal");
		IDENTIFIERS.put("ThrowPotion", "potion");
		IDENTIFIERS.put("ThrownExpBottle", "xp_bottle");
		IDENTIFIERS.put("ItemFrame", "item_frame");
		IDENTIFIERS.put("WitherSkull", "wither_skull");
		IDENTIFIERS.put("PrimedTNT", "tnt");
		IDENTIFIERS.put("FallingSand", "falling_block");
		IDENTIFIERS.put("FireworksRocketEntity", "fireworks_rocket");
		IDENTIFIERS.put("SpectralArrow", "spectral_arrow");
		IDENTIFIERS.put("ShulkerBullet", "shulker_bullet");
		IDENTIFIERS.put("DragonFireball", "dragon_fireball");
		IDENTIFIERS.put("ArmorStand", "armor_stand");
		IDENTIFIERS.put("Boat", "boat");
		IDENTIFIERS.put("MinecartRideable", "minecart");
		IDENTIFIERS.put("MinecartChest", "chest_minecart");
		IDENTIFIERS.put("MinecartFurnace", "furnace_minecart");
		IDENTIFIERS.put("MinecartTNT", "tnt_minecart");
		IDENTIFIERS.put("MinecartSpawner", "spawner_minecart");
		IDENTIFIERS.put("MinecartHopper", "hopper_minecart");
		IDENTIFIERS.put("MinecartCommandBlock", "commandblock_minecart");
		IDENTIFIERS.put("Mob", "mob");
		IDENTIFIERS.put("Monster", "monster");
		IDENTIFIERS.put("Creeper", "creeper");
		IDENTIFIERS.put("Skeleton", "skeleton");
		IDENTIFIERS.put("Spider", "spider");
		IDENTIFIERS.put("Giant", "giant");
		IDENTIFIERS.put("Zombie", "zombie");
		IDENTIFIERS.put("Slime", "slime");
		IDENTIFIERS.put("Ghast", "ghast");
		IDENTIFIERS.put("PigZombie", "zombie_pigman");
		IDENTIFIERS.put("Enderman", "enderman");
		IDENTIFIERS.put("CaveSpider", "cave_spider");
		IDENTIFIERS.put("Silverfish", "silverfish");
		IDENTIFIERS.put("Blaze", "blaze");
		IDENTIFIERS.put("LavaSlime", "magma_cube");
		IDENTIFIERS.put("EnderDragon", "ender_dragon");
		IDENTIFIERS.put("WitherBoss", "wither");
		IDENTIFIERS.put("Bat", "bat");
		IDENTIFIERS.put("Witch", "witch");
		IDENTIFIERS.put("Endermite", "endermite");
		IDENTIFIERS.put("Guardian", "guardian");
		IDENTIFIERS.put("Shulker", "shulker");
		IDENTIFIERS.put("Pig", "pig");
		IDENTIFIERS.put("Sheep", "sheep");
		IDENTIFIERS.put("Cow", "cow");
		IDENTIFIERS.put("Chicken", "chicken");
		IDENTIFIERS.put("Squid", "squid");
		IDENTIFIERS.put("Wolf", "wolf");
		IDENTIFIERS.put("MushroomCow", "mooshroom");
		IDENTIFIERS.put("SnowMan", "snowman");
		IDENTIFIERS.put("Ozelot", "ocelot");
		IDENTIFIERS.put("VillagerGolem", "villager_golem");
		IDENTIFIERS.put("EntityHorse", "horse");
		IDENTIFIERS.put("Rabbit", "rabbit");
		IDENTIFIERS.put("PolarBear", "polar_bear");
		IDENTIFIERS.put("Villager", "villager");
		IDENTIFIERS.put("EnderCrystal", "ender_crystal");
	}
}
