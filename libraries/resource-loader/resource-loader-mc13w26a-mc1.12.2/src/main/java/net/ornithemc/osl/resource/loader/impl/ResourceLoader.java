package net.ornithemc.osl.resource.loader.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.client.resource.metadata.ResourcePackMetadata;
import net.minecraft.client.resource.pack.ResourcePack;
import net.minecraft.client.resource.pack.ResourcePacks;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.resource.loader.api.ModResourcePack;

public class ResourceLoader implements ModInitializer, ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Resource Loader");

	private static final List<ModResourcePack> DEFAULT_MOD_RESOURCE_PACKS = new ArrayList<>();

	private static int packFormat = -1;

	@Override
	public void initClient() {
		MinecraftClientEvents.READY.register(minecraft -> {
			try {
				ResourcePacks resourcePacks = minecraft.getResourcePacks();
				ResourceMetadataSerializerRegistry metadataSerializers = resourcePacks.metadataSerializers;

				ResourcePack defaultPack = resourcePacks.defaultPack;
				ResourcePackMetadata metadata = defaultPack.getMetadataSection(metadataSerializers, "pack");

				packFormat = metadata.getFormat();
			} catch (IOException e) {
				packFormat = 0;
				LOGGER.info("unable to parse resource pack format from default resource pack; using default value of " + packFormat);
			}
		});
	}

	@Override
	public void init() {
		// empty impl
	}

	public static boolean addDefaultModResourcePack(ModResourcePack pack) {
		return DEFAULT_MOD_RESOURCE_PACKS.add(pack);
	}

	public static List<ModResourcePack> getDefaultModResourcePacks() {
		return Collections.unmodifiableList(DEFAULT_MOD_RESOURCE_PACKS);
	}

	public static int getPackFormat() {
		return packFormat;
	}
}
