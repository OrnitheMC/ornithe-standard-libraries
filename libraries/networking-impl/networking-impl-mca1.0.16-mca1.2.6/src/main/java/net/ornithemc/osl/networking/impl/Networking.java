package net.ornithemc.osl.networking.impl;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.mixin.common.PacketAccessor;

public class Networking implements ModInitializer, ClientModInitializer, ServerModInitializer {

	@Override
	public void init() {
		PacketAccessor.register(Constants.CUSTOM_PAYLOAD_PACKET_ID, CustomPayloadPacket.class);
	}

	@Override
	public void initClient() {
		MinecraftClientEvents.START.register(ClientPlayNetworkingImpl::setUp);
		MinecraftClientEvents.STOP.register(ClientPlayNetworkingImpl::destroy);
		ClientPlayNetworkingImpl.setUpPacketFactory(CustomPayloadPacket::new);
		ClientPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (context, payload) -> {
			// ensure the handshake is processed on the main thread,
			// where the network handler is guaranteed to be set
			context.ensureOnMainThread();

			// send channel registration data as a response to receiving server channel registration data
			ClientPlayNetworkingImpl.sendNoCheck(HandshakePayload.CHANNEL, HandshakePayload.client());

			((NetworkHandlerAccess)context.networkHandler()).osl$networking$registerChannels(payload.channels);
			ClientConnectionEvents.PLAY_READY.invoker().accept(context.minecraft());
		});
	}

	@Override
	public void initServer() {
		// no-op
	}
}
