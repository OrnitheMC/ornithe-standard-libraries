package net.ornithemc.osl.networking.impl;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.lifecycle.api.MinecraftEvents;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IServerPlayNetworkHandler;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class Networking implements ModInitializer {

	@Override
	public void init() {
		MinecraftEvents.START.register(server -> {
			ServerPlayNetworkingImpl.setUp(server);
		});
		MinecraftEvents.STOP.register(server -> {
			ServerPlayNetworkingImpl.destroy(server);
		});
		ServerPlayNetworking.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (server, handler, player, payload) -> {
			// send channel registration data as a response to receiving client channel registration data
			ServerPlayNetworkingImpl.doSend(player, HandshakePayload.CHANNEL, HandshakePayload.server());

			((IServerPlayNetworkHandler)handler).osl$networking$registerClientChannels(payload.channels);
			ServerConnectionEvents.PLAY_READY.invoker().accept(server, player);

			return true;
		});
	}
}
