package net.ornithemc.osl.networking.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.network.packet.Packet;

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
		ClientPlayNetworking.registerListener(CommonChannels.CHANNELS, (minecraft, handler, data) -> {
			// send channel registration data as a response to receiving server channel registration data
			ClientPlayNetworkingImpl.doSend(CommonChannels.CHANNELS, response -> {
				Networking.writeChannels(response, ClientPlayNetworkingImpl.LISTENERS.keySet());
			});

			((IClientNetworkHandler)handler).osl$networking$registerServerChannels(readChannels(data));
			ClientConnectionEvents.PLAY_READY.invoker().accept(minecraft);

			return true;
		});
	}

	public static Set<String> readChannels(DataInputStream data) throws IOException {
		Set<String> channels = new LinkedHashSet<>();
		int channelCount = data.readInt();

		if (channelCount > 0) {
			for (int i = 0; i < channelCount; i++) {
				channels.add(Packet.readString(data, 20));
			}
		}

		return channels;
	}

	public static void writeChannels(DataOutputStream data, Set<String> channels) throws IOException {
		data.writeInt(channels.size());

		for (String channel : channels) {
			Packet.writeString(channel, data);
		}
	}
}
