package net.ornithemc.osl.resource.loader.api;

import org.quiltmc.loader.api.ModMetadata;

import net.minecraft.client.resource.pack.ResourcePack;

public interface ModResourcePack extends ResourcePack {

	ModMetadata getModMetadata();

}
