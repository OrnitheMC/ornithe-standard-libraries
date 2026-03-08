package net.ornithemc.osl.resource.loader.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.resource.loader.api.ModTexturePack;
import net.ornithemc.osl.resource.loader.api.ResourceLoaderEvents;

public class ResourceLoader {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Resource Loader");

	private static List<ModTexturePack> DEFAULT_MOD_TEXTURE_PACKS;

	public static void resetDefaultTexturePacks() {
		DEFAULT_MOD_TEXTURE_PACKS = null;
	}

	public static List<ModTexturePack> getDefaultModResourcePacks() {
		if (DEFAULT_MOD_TEXTURE_PACKS == null) {
			DEFAULT_MOD_TEXTURE_PACKS = new ArrayList<>();

			for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
				if ("builtin".equals(mod.getMetadata().getType())) {
					continue;
				}

				DEFAULT_MOD_TEXTURE_PACKS.add(new BuiltInModTexturePack(mod));
			}

			ResourceLoaderEvents.ADD_DEFAULT_TEXTURE_PACKS.invoker().accept(DEFAULT_MOD_TEXTURE_PACKS::add);
		}

		return Collections.unmodifiableList(DEFAULT_MOD_TEXTURE_PACKS);
	}
}
