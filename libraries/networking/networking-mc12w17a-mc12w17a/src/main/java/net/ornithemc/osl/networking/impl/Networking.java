package net.ornithemc.osl.networking.impl;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.interfaces.mixin.INetworkHandler;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class Networking implements ModInitializer, ClientModInitializer, ServerModInitializer {

	@Override
	public void init() {
		// empty impl
	}

	@Override
	public void initClient() {
		MinecraftClientEvents.START.register(minecraft -> {
			ClientPlayNetworkingImpl.setUp(minecraft);
		});
		MinecraftClientEvents.STOP.register(minecraft -> {
			ClientPlayNetworkingImpl.destroy(minecraft);
		});
		ClientPlayNetworking.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (minecraft, handler, payload) -> {
			((INetworkHandler)handler).osl$networking$registerChannels(payload.channels);
			ClientConnectionEvents.PLAY_READY.invoker().accept(minecraft);

			return true;
		});
	}

	@Override
	public void initServer() {
		MinecraftServerEvents.START.register(server -> {
			ServerPlayNetworkingImpl.setUp(server);
		});
		MinecraftServerEvents.STOP.register(server -> {
			ServerPlayNetworkingImpl.destroy(server);
		});
		ServerPlayNetworking.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (server, handler, player, payload) -> {
			// send channel registration data as a response to receiving client channel registration data
			ServerPlayNetworkingImpl.doSend(player, HandshakePayload.CHANNEL, HandshakePayload.server());

			((INetworkHandler)handler).osl$networking$registerChannels(payload.channels);
			ServerConnectionEvents.PLAY_READY.invoker().accept(server, player);

			return true;
		});
	}
}
