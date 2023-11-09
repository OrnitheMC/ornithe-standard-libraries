package net.ornithemc.osl.resource.loader.api;

import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.resource.pack.Pack;

public interface ModPack extends Pack {

	ModMetadata getModMetadata();

}
