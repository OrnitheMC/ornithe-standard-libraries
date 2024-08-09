package net.ornithemc.osl.config.impl.client;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.impl.ConfigManagerImpl;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;

public class ClientConfigInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		MinecraftClientEvents.START.register(minecraft -> {
			ConfigManagerImpl.setUp(ConfigScope.GLOBAL, Minecraft.getGameDirectory().toPath());
			ConfigManagerImpl.load(ConfigScope.GLOBAL, LoadingPhase.START);
		});
		MinecraftClientEvents.READY.register(minecraft -> {
			ConfigManagerImpl.load(ConfigScope.GLOBAL, LoadingPhase.READY);
		});
		MinecraftClientEvents.STOP.register(minecraft -> {
			ConfigManagerImpl.unload(ConfigScope.GLOBAL);
			ConfigManagerImpl.destroy(ConfigScope.GLOBAL);
		});
	}
}
