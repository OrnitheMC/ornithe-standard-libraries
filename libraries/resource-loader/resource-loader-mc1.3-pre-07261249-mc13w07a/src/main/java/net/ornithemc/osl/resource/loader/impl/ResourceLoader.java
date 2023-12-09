package net.ornithemc.osl.resource.loader.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class ResourceLoader implements ModInitializer, ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Resource Loader");

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
}
