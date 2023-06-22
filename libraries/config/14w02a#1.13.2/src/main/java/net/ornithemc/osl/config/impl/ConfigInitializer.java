package net.ornithemc.osl.config.impl;

import net.ornithemc.osl.config.api.ConfigManager;
import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.lifecycle.api.server.ServerWorldEvents;

public class ConfigInitializer implements ModInitializer {

	@Override
	public void init() {
		ServerWorldEvents.LOAD.register(world -> {
			ConfigManager.setUp(ConfigScope.WORLD, world.getStorage().getDir().toPath());
			ConfigManager.load(ConfigScope.WORLD, LoadingPhase.START);
		});
		ServerWorldEvents.READY.register(world -> {
			ConfigManager.load(ConfigScope.WORLD, LoadingPhase.READY);
		});
	}
}
