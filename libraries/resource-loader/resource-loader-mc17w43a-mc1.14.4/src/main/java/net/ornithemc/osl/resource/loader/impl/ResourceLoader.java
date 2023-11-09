package net.ornithemc.osl.resource.loader.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.resource.pack.ResourcePacks;
import net.minecraft.resource.pack.BuiltInPack;
import net.minecraft.resource.pack.PackType;
import net.minecraft.resource.pack.metadata.PackMetadataSection;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.resource.loader.api.ModPack;

public class ResourceLoader implements ModInitializer, ClientModInitializer {

	private static final Logger LOGGER = LogManager.getLogger("OSL|Resource Loader");

	private static final List<ModPack> DEFAULT_MOD_PACKS = new ArrayList<>();

	private static int resourcePackFormat = -1;
	private static int dataPackFormat = -1;

	@Override
	public void initClient() {
		MinecraftClientEvents.READY.register(minecraft -> {
			try {
				ResourcePacks resourcePacks = minecraft.getResourcePacks();

				BuiltInPack defaultPack = resourcePacks.getDefaultPack();
				PackMetadataSection metadata = defaultPack.getMetadataSection(PackMetadataSection.SERIALIZER);

				resourcePackFormat = metadata.getFormat();
			} catch (IOException e) {
				resourcePackFormat = 0;
				LOGGER.info("unable to parse resource pack format from default resource pack; using default value of " + resourcePackFormat, e);
			}
		});
	}

	@Override
	public void init() {
		MinecraftServerEvents.READY.register(server -> {
			try {
				@SuppressWarnings("resource")
				BuiltInPack defaultPack = new BuiltInPack("minecraft");
				PackMetadataSection metadata = defaultPack.getMetadataSection(PackMetadataSection.SERIALIZER);

				dataPackFormat = metadata.getFormat();
			} catch (IOException e) {
				dataPackFormat = 0;
				LOGGER.info("unable to parse data pack format from default data pack; using default value of " + dataPackFormat, e);
			}
		});
	}

	public static boolean addDefaultModPack(ModPack pack) {
		return DEFAULT_MOD_PACKS.add(pack);
	}

	public static List<ModPack> getDefaultModPacks() {
		return Collections.unmodifiableList(DEFAULT_MOD_PACKS);
	}

	public static int getPackFormat(PackType type) {
		switch (type) {
		case CLIENT_RESOURCES:
			return getResourcePackFormat();
		case SERVER_DATA:
			return getDataPackFormat();
		default:
			throw new IllegalStateException("unable to get format for unknown pack type " + type);
		}
	}

	public static int getResourcePackFormat() {
		return resourcePackFormat;
	}

	public static int getDataPackFormat() {
		return dataPackFormat;
	}
}
