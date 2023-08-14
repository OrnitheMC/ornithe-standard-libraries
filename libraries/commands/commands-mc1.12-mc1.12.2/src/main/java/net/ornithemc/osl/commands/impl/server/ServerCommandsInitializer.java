package net.ornithemc.osl.commands.impl.server;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;

public class ServerCommandsInitializer implements ModInitializer {

	@Override
	public void init() {
		MinecraftServerEvents.READY.register(server -> {
			ServerCommandManagerImpl.setUp(server);
		});
		MinecraftServerEvents.STOP.register(server -> {
			ServerCommandManagerImpl.destroy(server);
		});
	}
}
