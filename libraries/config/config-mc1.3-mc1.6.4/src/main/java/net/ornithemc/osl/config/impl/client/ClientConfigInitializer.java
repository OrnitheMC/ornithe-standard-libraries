package net.ornithemc.osl.config.impl.client;

import java.io.File;

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
			// this is one hell of a hack to make this code compatible with 1.5 as well as 1.6
			// the code is compiled against 1.5.2, so that the Minecraft.getWorkingDirectory method exists
			// in 1.5.2 gameDir is null at this time, so it will call Minecraft.getWorkingDirectory
			// in 1.6.4 gameDir is not null so it will not have to call Minecraft.getWorkingDirectory,
			// which does not exist in that version anyway
			File gameDir = (minecraft.gameDir == null) ? Minecraft.getWorkingDirectory() : minecraft.gameDir;
			ConfigManagerImpl.setUp(ConfigScope.GLOBAL, gameDir.toPath());
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
