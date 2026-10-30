package net.ornithemc.osl.resource.loader.impl;

import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.resource.loader.impl.resource.pack.ModResourcePack;

public class DefaultTexturePack extends TexturePack {

	public DefaultTexturePack() {
		super(new ModResourcePack(FabricLoader.getInstance().getModContainer("minecraft").get(), ".", "Default"));
	}

	@Override
	void initMetadata() {
		this.descriptionLine1 = "The default look of Minecraft";
	}
}
