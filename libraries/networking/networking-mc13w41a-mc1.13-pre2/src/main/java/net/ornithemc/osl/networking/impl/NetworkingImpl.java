package net.ornithemc.osl.networking.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientPlayNetworkHandler;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IServerPlayNetworkHandler;

public class NetworkingImpl implements ModInitializer, ClientModInitializer {

	@Override
	public void init() {
		ServerPlayNetworking.registerListener(CommonChannels.CHANNELS, (server, handler, player, data) -> {
			Set<String> channels = readChannels(data);

			if (!channels.isEmpty()) {
				((IServerPlayNetworkHandler)handler).osl$networking$registerClientChannels(channels);
			}

			return true;
		});
	}

	@Override
	public void initClient() {
		ClientPlayNetworking.registerListener(CommonChannels.CHANNELS, (minecraft, handler, data) -> {
			Set<String> channels = readChannels(data);

			if (!channels.isEmpty()) {
				((IClientPlayNetworkHandler)handler).osl$networking$registerServerChannels(channels);
			}

			return true;
		});
	}

	public static Set<String> readChannels(PacketByteBuf data) {
		Set<String> channels = new LinkedHashSet<>();
		int channelCount = data.readInt();

		if (channelCount > 0) {
			for (int i = 0; i < channelCount; i++) {
				channels.add(data.readString(20));
			}
		}

		return channels;
	}

	public static void writeChannels(PacketByteBuf data, Set<String> channels) {
		data.writeInt(channels.size());

		for (String channel : channels) {
			data.writeString(channel);
		}
	}
}
