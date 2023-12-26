package net.ornithemc.osl.resource.loader.api;

import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.client.resource.pack.TexturePack;

public interface ModTexturePack extends TexturePack {

	ModMetadata getModMetadata();

}
