package net.ornithemc.osl.resource.loader.api;

import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.client.resource.pack.ResourcePack;

public interface ModResourcePack extends ResourcePack {

	ModMetadata getModMetadata();

}
