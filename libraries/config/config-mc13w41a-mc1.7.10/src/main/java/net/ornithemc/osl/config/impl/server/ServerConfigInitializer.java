package net.ornithemc.osl.config.impl.server;

import java.io.File;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.impl.ConfigManagerImpl;
import net.ornithemc.osl.config.impl.interfaces.mixin.IMinecraftServer;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;

public class ServerConfigInitializer implements ServerModInitializer {

	@Override
	public void initServer() {
		MinecraftServerEvents.START.register(server -> {
			File runDir = ((IMinecraftServer)server).osl$config$getRunDir();

			ConfigManagerImpl.setUp(ConfigScope.GLOBAL, runDir.toPath());
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
