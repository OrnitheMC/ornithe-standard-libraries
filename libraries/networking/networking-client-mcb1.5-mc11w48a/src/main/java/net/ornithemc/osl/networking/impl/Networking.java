package net.ornithemc.osl.networking.impl;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.MinecraftEvents;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientNetworkHandler;
import net.ornithemc.osl.networking.impl.mixin.common.PacketAccessor;

public class Networking implements ModInitializer, ClientModInitializer {

	@Override
	public void init() {
		PacketAccessor.register(Constants.CUSTOM_PAYLOAD_PACKET_ID, true, true, CustomPayloadPacket.class);
	}

	@Override
	public void initClient() {
		MinecraftEvents.START.register(minecraft -> {
			ClientPlayNetworkingImpl.setUp(minecraft);
		});
		MinecraftEvents.STOP.register(minecraft -> {
			ClientPlayNetworkingImpl.destroy(minecraft);
		});
		ClientPlayNetworking.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (minecraft, handler, payload) -> {
			// send channel registration data as a response to receiving server channel registration data
			ClientPlayNetworking.doSend(HandshakePayload.CHANNEL, HandshakePayload.client());

			((IClientNetworkHandler)handler).osl$networking$registerServerChannels(payload.channels);
			ClientConnectionEvents.PLAY_READY.invoker().accept(minecraft);

			return true;
		});
	}
}
