package net.ornithemc.osl.resource.loader.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.resource.loader.api.ModTexturePack;
import net.ornithemc.osl.resource.loader.api.ResourceLoaderEvents;

public class ResourceLoader implements ModInitializer, ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Resource Loader");

	private static List<ModTexturePack> DEFAULT_MOD_TEXTURE_PACKS;

	// empty impls needed because the mod.json is shared across all modules
	// and the 1.6+ versions register listeners to lifecycle events here

	@Override
	public void initClient() {
		// empty impl
	}

	@Override
	public void init() {
		// empty impl
	}

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
