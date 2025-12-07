package net.ornithemc.osl.networking.impl;

import net.minecraft.network.packet.CustomPayloadPacket;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class Networking implements ModInitializer, ClientModInitializer, ServerModInitializer {

	@Override
	public void init() {
		// no-op
	}

	@Override
	public void initClient() {
		MinecraftClientEvents.START.register(ClientPlayNetworkingImpl::setUp);
		MinecraftClientEvents.STOP.register(ClientPlayNetworkingImpl::destroy);
		ClientPlayNetworkingImpl.setUpPacketFactory(Networking::newCustomPayloadPacket);
		ClientPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (context, payload) -> {
			((NetworkHandlerAccess)context.networkHandler()).osl$networking$registerChannels(payload.channels);
			ClientConnectionEvents.PLAY_READY.invoker().accept(context.minecraft());
		});
	}

	@Override
	public void initServer() {
		MinecraftServerEvents.START.register(ServerPlayNetworkingImpl::setUp);
		MinecraftServerEvents.STOP.register(ServerPlayNetworkingImpl::destroy);
		ServerPlayNetworkingImpl.setUpPacketFactory(Networking::newCustomPayloadPacket);
		ServerPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (context, payload) -> {
			// send channel registration data as a response to receiving client channel registration data
			ServerPlayNetworkingImpl.sendNoCheck(context.player(), HandshakePayload.CHANNEL, HandshakePayload.server());

			((NetworkHandlerAccess)context.networkHandler()).osl$networking$registerChannels(payload.channels);
			ServerConnectionEvents.PLAY_READY.invoker().accept(context.server(), context.player());
		});
	}

	private static CustomPayloadPacket newCustomPayloadPacket(NamespacedIdentifier channel, byte[] data) {
		return new CustomPayloadPacket(StringChannelIdentifierParser.toString(channel), data);
	}
}
