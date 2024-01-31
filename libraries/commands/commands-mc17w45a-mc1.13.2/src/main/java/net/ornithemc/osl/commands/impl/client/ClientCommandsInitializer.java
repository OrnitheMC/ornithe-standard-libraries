package net.ornithemc.osl.commands.impl.client;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;

public class ClientCommandsInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		ClientConnectionEvents.LOGIN.register(minecraft -> {
			ClientCommandManagerImpl.setUp(minecraft);
		});
		ClientConnectionEvents.DISCONNECT.register(minecraft -> {
			ClientCommandManagerImpl.destroy(minecraft);
		});
	}
}
