package net.ornithemc.osl.resource.loader.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ornithemc.osl.resource.loader.api.ModResourcePack;

public class ResourceLoader {

	public static final Logger LOGGER = LogManager.getLogger("osl:resource-loader");

	private static final List<ModResourcePack> DEFAULT_MOD_RESOURCE_PACKS = new ArrayList<>();

	public static boolean addDefaultModResourcePack(ModResourcePack pack) {
		return DEFAULT_MOD_RESOURCE_PACKS.add(pack);
	}

	public static List<ModResourcePack> getDefaultModResourcePacks() {
		return Collections.unmodifiableList(DEFAULT_MOD_RESOURCE_PACKS);
	}
}
