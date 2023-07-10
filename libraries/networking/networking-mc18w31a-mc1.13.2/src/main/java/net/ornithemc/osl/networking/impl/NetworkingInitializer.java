package net.ornithemc.osl.networking.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientPlayNetworkHandler;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IServerPlayNetworkHandler;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

public class NetworkingInitializer implements ModInitializer, ClientModInitializer {

	@Override
	public void init() {
		MinecraftServerEvents.START.register(server -> {
			ServerPlayNetworkingImpl.setUp(server);
		});
		MinecraftServerEvents.STOP.register(server -> {
			ServerPlayNetworkingImpl.destroy(server);
		});
		ServerPlayNetworking.registerListener(CommonChannels.CHANNELS, (server, handler, player, data) -> {
			Set<Identifier> channels = readChannels(data);

			if (!channels.isEmpty()) {
				((IServerPlayNetworkHandler)handler).osl$networking$registerClientChannels(channels);
			}

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
			Set<Identifier> channels = readChannels(data);

			if (!channels.isEmpty()) {
				((IClientPlayNetworkHandler)handler).osl$networking$registerServerChannels(channels);
			}

			return true;
		});
	}

	public static Set<Identifier> readChannels(PacketByteBuf data) {
		Set<Identifier> channels = new LinkedHashSet<>();
		int channelCount = data.readInt();

		if (channelCount > 0) {
			for (int i = 0; i < channelCount; i++) {
				channels.add(data.readIdentifier());
			}
		}

		return channels;
	}

	public static void writeChannels(PacketByteBuf data, Set<Identifier> channels) {
		data.writeInt(channels.size());

		for (Identifier channel : channels) {
			data.writeIdentifier(channel);
		}
	}
}
