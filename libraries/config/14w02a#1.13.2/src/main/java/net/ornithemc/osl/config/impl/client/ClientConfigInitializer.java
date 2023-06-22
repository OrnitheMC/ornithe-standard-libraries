package net.ornithemc.osl.config.impl.client;

import net.ornithemc.osl.config.api.ConfigManager;
import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;

public class ClientConfigInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		MinecraftClientEvents.START.register(minecraft -> {
			ConfigManager.setUp(ConfigScope.GLOBAL, minecraft.runDir.toPath());
			ConfigManager.load(ConfigScope.GLOBAL, LoadingPhase.START);
		});
		MinecraftClientEvents.READY.register(minecraft -> {
			ConfigManager.load(ConfigScope.GLOBAL, LoadingPhase.READY);
		});
	}
}
