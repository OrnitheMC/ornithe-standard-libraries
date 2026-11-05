package net.ornithemc.osl.networking.impl;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.mixin.common.PacketAccessor;
import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class Networking implements ModInitializer, ClientModInitializer, ServerModInitializer {

	@Override
	public void init() {
		PacketAccessor.register(Constants.CUSTOM_PAYLOAD_PACKET_ID, CustomPayloadPacket.class);
	}

	@Override
	public void initClient() {
		// no-op
	}

	@Override
	public void initServer() {
		MinecraftServerEvents.START.register(ServerPlayNetworkingImpl::setUp);
		MinecraftServerEvents.STOP.register(ServerPlayNetworkingImpl::destroy);
		ServerPlayNetworkingImpl.setUpPacketFactory(CustomPayloadPacket::new);
		ServerPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (context, payload) -> {
			ServerNetworkHandlerAccess networkHandler = (ServerNetworkHandlerAccess) context.networkHandler();
			ServerConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

			networkHandler.osl$networking$registerChannels(payload.channels);
			ServerConnectionEvents.PLAY_READY.invoker().accept(connectionContext);
		});
	}
}
