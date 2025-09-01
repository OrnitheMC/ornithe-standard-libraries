package net.ornithemc.osl.networking.impl;

import net.minecraft.network.packet.CustomPayloadPacket;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.StringChannelParser;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class Networking implements ModInitializer, ClientModInitializer, ServerModInitializer {

	@Override
	public void init() {
		MinecraftServerEvents.START.register(ServerPlayNetworkingImpl::setUp);
		MinecraftServerEvents.STOP.register(ServerPlayNetworkingImpl::destroy);
		ServerPlayNetworkingImpl.setUpPacketFactory(Networking::newCustomPayloadPacket);
		ServerPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (server, handler, player, payload) -> {
			// send channel registration data as a response to receiving client channel registration data
			ServerPlayNetworkingImpl.sendNoCheck(player, HandshakePayload.CHANNEL, HandshakePayload.server());

			((NetworkHandlerAccess)handler).osl$networking$registerChannels(payload.channels);
			ServerConnectionEvents.PLAY_READY.invoker().accept(server, player);

			return true;
		});
	}

	@Override
	public void initClient() {
		MinecraftClientEvents.START.register(ClientPlayNetworkingImpl::setUp);
		MinecraftClientEvents.STOP.register(ClientPlayNetworkingImpl::destroy);
		ClientPlayNetworkingImpl.setUpPacketFactory(Networking::newCustomPayloadPacket);
		ClientPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (minecraft, handler, payload) -> {
			((NetworkHandlerAccess)handler).osl$networking$registerChannels(payload.channels);
			ClientConnectionEvents.PLAY_READY.invoker().accept(minecraft);

			return true;
		});
	}

	@Override
	public void initServer() {
		// no-op
	}

	private static CustomPayloadPacket newCustomPayloadPacket(Channel channel, byte[] data) {
		return new CustomPayloadPacket(StringChannelParser.toString(channel), data);
	}
}
