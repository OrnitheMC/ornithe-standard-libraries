package net.ornithemc.osl.resource.loader.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.loader.api.FabricLoader;

public class ResourceLoader {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Resource Loader");
	public static final boolean DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();

}
