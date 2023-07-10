package net.ornithemc.osl.config.impl;

import net.minecraft.world.storage.WorldStorage;
import net.minecraft.world.storage.WorldStorageSource;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;

public class ConfigInitializer implements ModInitializer {

	@Override
	public void init() {
		MinecraftServerEvents.LOAD_WORLD.register(server -> {
			String worldDirName = server.getWorldDirName();
			WorldStorageSource storageSource = server.getWorldStorageSource();
			WorldStorage storage = storageSource.get(worldDirName, false);

			ConfigManagerImpl.setUp(ConfigScope.WORLD, storage.getDir().toPath());
			ConfigManagerImpl.load(ConfigScope.WORLD, LoadingPhase.START);
		});
		MinecraftServerEvents.READY_WORLD.register(server -> {
			ConfigManagerImpl.load(ConfigScope.WORLD, LoadingPhase.READY);
		});
		MinecraftServerEvents.STOP.register(server -> {
			ConfigManagerImpl.unload(ConfigScope.WORLD);
			ConfigManagerImpl.destroy(ConfigScope.WORLD);
		});
	}
}
