package net.ornithemc.osl.config.api;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.impl.ConfigManagerImpl;

public final class ConfigManager {

	public static void register(Config config) {
		ConfigManagerImpl.register(config);
	}

	public static void save(Config config) {
		ConfigManagerImpl.save(config);
	}
}
