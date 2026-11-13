package net.ornithemc.osl.networking.impl;

import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.ClientNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.mixin.common.PacketAccessor;
import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class Networking implements ModInitializer, ClientModInitializer, ServerModInitializer {

	public static final boolean PACKET_FLOW_EXISTS = MinecraftVersion.resolve().compareTo("b1.5") >= 0;
	public static final boolean CUSTOM_PAYLOAD_EXISTS = MinecraftVersion.resolve().compareTo("11w49a") >= 0;
	public static final boolean LOCAL_SERVER_EXISTS = MinecraftVersion.resolve().compareTo("12w18a") >= 0;
	public static final boolean INTEGRATED_SERVER_EXISTS = MinecraftVersion.resolve().compareTo("12w21a") >= 0;

	@Override
	public void init() {
		if (!CUSTOM_PAYLOAD_EXISTS) {
			if (PACKET_FLOW_EXISTS) {
				PacketAccessor.register(Constants.CUSTOM_PAYLOAD_PACKET_ID, true, true, CustomPayloadPacket.class);
			} else {
				PacketAccessor.register(Constants.CUSTOM_PAYLOAD_PACKET_ID, CustomPayloadPacket.class);
			}
		}
		if (LOCAL_SERVER_EXISTS) {
			this.setupServer();
		}
	}

	public void setupServer() {
		MinecraftServerEvents.START.register(ServerPlayNetworkingImpl::setUp);
		MinecraftServerEvents.STOP.register(ServerPlayNetworkingImpl::destroy);
		ServerPlayNetworkingImpl.setUpPacketFactory(Networking::newCustomPayloadPacket);
		ServerPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (context, payload) -> {
			if (CUSTOM_PAYLOAD_EXISTS) {
				// send channel registration data as a response to receiving client channel registration data
				ServerPlayNetworkingImpl.sendNoCheck(context.player(), HandshakePayload.CHANNEL, HandshakePayload.server());
			}

			ServerNetworkHandlerAccess networkHandler = (ServerNetworkHandlerAccess) context.networkHandler();
			ServerConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

			networkHandler.osl$networking$registerChannels(payload.channels);
			ServerConnectionEvents.PLAY_READY.invoker().accept(connectionContext);
		});
	}

	@Override
	public void initClient() {
		MinecraftClientEvents.START.register(ClientPlayNetworkingImpl::setUp);
		MinecraftClientEvents.STOP.register(ClientPlayNetworkingImpl::destroy);
		ClientPlayNetworkingImpl.setUpPacketFactory(Networking::newCustomPayloadPacket);
		ClientPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (context, payload) -> {
			if (!CUSTOM_PAYLOAD_EXISTS) {
				// ensure the handshake is processed on the main thread,
				// where the network handler is guaranteed to be set
				context.ensureOnMainThread();

				// send channel registration data as a response to receiving server channel registration data
				ClientPlayNetworkingImpl.sendNoCheck(HandshakePayload.CHANNEL, HandshakePayload.client());
			}

			ClientNetworkHandlerAccess networkHandler = (ClientNetworkHandlerAccess) context.networkHandler();
			ClientConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

			networkHandler.osl$networking$registerChannels(payload.channels);
			ClientConnectionEvents.PLAY_READY.invoker().accept(connectionContext);
		});
	}

	@Override
	public void initServer() {
		this.setupServer();
	}

	private static Packet newCustomPayloadPacket(NamespacedIdentifier channel, byte[] data) {
		if (CUSTOM_PAYLOAD_EXISTS) {
			return CustomPayloadPackets.of(channel, data);
		} else {
			return new CustomPayloadPacket(channel, data);
		}
	}
}
