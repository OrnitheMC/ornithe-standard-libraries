package net.ornithemc.osl.networking.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.network.packet.Packet;

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
		ServerPlayNetworking.registerListener(CommonChannels.CHANNELS, (server, handler, player, data) -> {
			// send channel registration data as a response to receiving client channel registration data
			ServerPlayNetworkingImpl.doSend(player, CommonChannels.CHANNELS, response -> {
				Networking.writeChannels(response, ServerPlayNetworkingImpl.LISTENERS.keySet());
			});

			((IServerPlayNetworkHandler)handler).osl$networking$registerClientChannels(readChannels(data));
			ServerConnectionEvents.PLAY_READY.invoker().accept(server, player);

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
