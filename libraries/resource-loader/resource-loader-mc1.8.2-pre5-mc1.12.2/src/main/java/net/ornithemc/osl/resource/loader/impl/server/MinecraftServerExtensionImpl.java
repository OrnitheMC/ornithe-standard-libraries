package net.ornithemc.osl.resource.loader.impl.server;

import net.minecraft.world.gen.loot.LootTables;

import net.ornithemc.osl.resource.loader.api.server.MinecraftServerExtension;

public interface MinecraftServerExtensionImpl extends MinecraftServerExtension {

	@Override
	default LootTables getLootTables() {
		throw new AbstractMethodError();
	}

	@Override
	default void reloadResources() {
		throw new AbstractMethodError();
	}
}
