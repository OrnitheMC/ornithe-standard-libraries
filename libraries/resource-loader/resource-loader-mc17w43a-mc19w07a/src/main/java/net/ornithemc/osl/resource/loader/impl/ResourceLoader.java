package net.ornithemc.osl.resource.loader.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resource.pack.PackType;

import net.ornithemc.osl.resource.loader.api.ModPack;

public class ResourceLoader {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Resource Loader");

	private static final List<ModPack> DEFAULT_MOD_PACKS = new ArrayList<>();

	private static int resourcePackFormat = -1;
	private static int dataPackFormat = -1;

	public static void setResourcePackFormat(int format) {
		if (resourcePackFormat < 0) {
			resourcePackFormat = format;
		}
	}

	public static void setDataPackFormat(int format) {
		if (dataPackFormat < 0) {
			dataPackFormat = format;
		}
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
