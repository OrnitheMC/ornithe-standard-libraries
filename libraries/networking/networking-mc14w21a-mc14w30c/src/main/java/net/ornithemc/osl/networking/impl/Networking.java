package net.ornithemc.osl.networking.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientPlayNetworkHandler;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IServerPlayNetworkHandler;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class Networking implements ModInitializer, ClientModInitializer {

	@Override
	public void init() {
		MinecraftServerEvents.START.register(server -> {
			ServerPlayNetworkingImpl.setUp(server);
		});
		MinecraftServerEvents.STOP.register(server -> {
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

	@Override
	public void initClient() {
		MinecraftClientEvents.START.register(minecraft -> {
			ClientPlayNetworkingImpl.setUp(minecraft);
		});
		MinecraftClientEvents.STOP.register(minecraft -> {
			ClientPlayNetworkingImpl.destroy(minecraft);
		});
		ClientPlayNetworking.registerListener(CommonChannels.CHANNELS, (minecraft, handler, data) -> {
			((IClientPlayNetworkHandler)handler).osl$networking$registerServerChannels(readChannels(data));
			ClientConnectionEvents.PLAY_READY.invoker().accept(minecraft);

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
