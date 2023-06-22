package net.ornithemc.osl.config.impl.server;

import net.ornithemc.osl.config.api.ConfigManager;
import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;

public class ServerConfigInitializer implements ServerModInitializer {

	@Override
	public void initServer() {
		MinecraftServerEvents.START.register(server -> {
			ConfigManager.setUp(ConfigScope.GLOBAL, server.getRunDir().toPath());
			ConfigManager.load(ConfigScope.GLOBAL, LoadingPhase.START);
		});
		MinecraftServerEvents.READY.register(server -> {
			ConfigManager.load(ConfigScope.GLOBAL, LoadingPhase.READY);
		});
	}
}
