package net.ornithemc.osl.config.impl.server;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.impl.ConfigManagerImpl;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;

public class ServerConfigInitializer implements ServerModInitializer {

	@Override
	public void initServer() {
		MinecraftServerEvents.START.register(server -> {
			ConfigManagerImpl.setUp(ConfigScope.GLOBAL, server.getRunDir().toPath());
			ConfigManagerImpl.load(ConfigScope.GLOBAL, LoadingPhase.START);
		});
		MinecraftServerEvents.READY.register(server -> {
			ConfigManagerImpl.load(ConfigScope.GLOBAL, LoadingPhase.READY);
		});
		MinecraftServerEvents.STOP.register(server -> {
			ConfigManagerImpl.unload(ConfigScope.GLOBAL);
			ConfigManagerImpl.destroy(ConfigScope.GLOBAL);
		});
	}
}
