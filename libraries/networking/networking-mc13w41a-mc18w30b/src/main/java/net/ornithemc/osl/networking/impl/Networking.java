package net.ornithemc.osl.networking.impl;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.IdentifierChannelIdentifierParser;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.ClientNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.mixin.client.CustomPayloadC2SPacketAccess;
import net.ornithemc.osl.networking.impl.mixin.common.CustomPayloadS2CPacketAccess;
import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class Networking implements ModInitializer, ClientModInitializer, ServerModInitializer {

	public static final boolean CLIENTBOUND_STRING_CHANNELS = MinecraftVersion.resolve().compareTo("1.13-pre3") < 0;
	public static final boolean SERVERBOUND_STRING_CHANNELS = MinecraftVersion.resolve().compareTo("1.13-pre4") < 0;
	public static final boolean CLIENTBOUND_BYTE_ARRAY_DATA = MinecraftVersion.resolve().compareTo("14w31a") < 0;
	public static final boolean SERVERBOUND_BYTE_ARRAY_DATA = MinecraftVersion.resolve().compareTo("14w31a") < 0;

	@Override
	public void init() {
		MinecraftServerEvents.START.register(ServerPlayNetworkingImpl::setUp);
		MinecraftServerEvents.STOP.register(ServerPlayNetworkingImpl::destroy);
		ServerPlayNetworkingImpl.setUpPacketFactory(Networking::newCustomPayloadS2CPacket);
		ServerPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (context, payload) -> {
			// send channel registration data as a response to receiving client channel registration data
			ServerPlayNetworkingImpl.sendNoCheck(context.player(), HandshakePayload.CHANNEL, HandshakePayload.server());

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
		ClientPlayNetworkingImpl.setUpPacketFactory(Networking::newCustomPayloadC2SPacket);
		ClientPlayNetworkingImpl.registerListener(HandshakePayload.CHANNEL, HandshakePayload::new, (context, payload) -> {
			ClientNetworkHandlerAccess networkHandler = (ClientNetworkHandlerAccess) context.networkHandler();
			ClientConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

			networkHandler.osl$networking$registerChannels(payload.channels);
			ClientConnectionEvents.PLAY_READY.invoker().accept(connectionContext);
		});
	}

	@Override
	public void initServer() {
		// no-op
	}

	private static Packet newCustomPayloadS2CPacket(NamespacedIdentifier channel, PacketBuffer data) {
		if (!CLIENTBOUND_STRING_CHANNELS) {
			return new CustomPayloadS2CPacket(IdentifierChannelIdentifierParser.toIdentifier(channel), PacketBuffers.unwrapped(data));
		} else if (!CLIENTBOUND_BYTE_ARRAY_DATA) {
			return CustomPayloadS2CPacketAccess.of(StringChannelIdentifierParser.toString(channel), PacketBuffers.unwrapped(data));
		} else {
			return CustomPayloadS2CPacketAccess.of(StringChannelIdentifierParser.toString(channel), PacketBuffers.unwrap(data));
		}
	}

	private static Packet newCustomPayloadC2SPacket(NamespacedIdentifier channel, PacketBuffer data) {
		if (!SERVERBOUND_STRING_CHANNELS) {
			return new CustomPayloadC2SPacket(IdentifierChannelIdentifierParser.toIdentifier(channel), PacketBuffers.unwrapped(data));
		} else if (!SERVERBOUND_BYTE_ARRAY_DATA) {
			return CustomPayloadC2SPacketAccess.of(StringChannelIdentifierParser.toString(channel), PacketBuffers.unwrapped(data));
		} else {
			return CustomPayloadC2SPacketAccess.of(StringChannelIdentifierParser.toString(channel), PacketBuffers.unwrap(data));
		}
	}
}
