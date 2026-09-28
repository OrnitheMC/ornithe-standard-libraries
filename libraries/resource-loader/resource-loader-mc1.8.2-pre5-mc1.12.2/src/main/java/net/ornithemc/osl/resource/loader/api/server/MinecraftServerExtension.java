package net.ornithemc.osl.resource.loader.api.server;

import net.minecraft.world.gen.loot.LootTables;

public interface MinecraftServerExtension {

	LootTables getLootTables();

	void reloadResources();

}
