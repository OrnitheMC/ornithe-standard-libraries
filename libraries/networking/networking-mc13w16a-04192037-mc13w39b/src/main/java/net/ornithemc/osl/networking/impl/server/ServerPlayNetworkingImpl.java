package net.ornithemc.osl.networking.impl.server;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.network.packet.CustomPayloadPacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.DataStreams;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking.ByteArrayListener;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking.StreamListener;
import net.ornithemc.osl.networking.impl.NetworkListener;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IServerPlayNetworkHandler;

public final class ServerPlayNetworkingImpl {

	private static MinecraftServer server;

	public static void setUp(MinecraftServer server) {
		if (ServerPlayNetworkingImpl.server == server) {
			throw new IllegalStateException("tried to set up server networking when it was already set up!");
		}

		ServerPlayNetworkingImpl.server = server;
	}

	public static void destroy(MinecraftServer server) {
		if (ServerPlayNetworkingImpl.server != server) {
			throw new IllegalStateException("tried to destroy server networking when it was not set up!");
		}

		ServerPlayNetworkingImpl.server = null;
	}

	public static final Map<String, NetworkListener<StreamListener, ByteArrayListener>> LISTENERS = new LinkedHashMap<>();

	public static void registerListener(String channel, StreamListener listener) {
		registerListener(channel, listener, null);
	}

	public static void registerListener(String channel, ByteArrayListener listener) {
		registerListener(channel, null, listener);
	}

	private static void registerListener(String channel, StreamListener buffer, ByteArrayListener array) {
		LISTENERS.compute(channel, (key, value) -> {
			if (value != null) {
				throw new IllegalStateException("there is already a listener on channel \'" + channel + "\'");
			}

			return new NetworkListener<>(buffer, array);
		});
	}

	public static void unregisterListener(String channel) {
		LISTENERS.remove(channel);
	}

	public static boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, CustomPayloadPacket packet) {
		NetworkListener<StreamListener, ByteArrayListener> listener = LISTENERS.get(packet.channel);

		if (listener != null) {
			if (listener.isStream()) {
				try {
					return listener.stream().handle(server, handler, player, DataStreams.input(packet.data));
				} catch (IOException e) {
					System.out.println("error handling custom payload on channel \'" + packet.channel + "\'");
					e.printStackTrace();
				}
			} else {
				return listener.array().handle(server, handler, player, packet.data);
			}
		}

		return false;
	}

	public static boolean canSend(ServerPlayerEntity player, String channel) {
		return ((IServerPlayNetworkHandler)player.networkHandler).osl$networking$isRegisteredClientChannel(channel);
	}

	public static void send(ServerPlayerEntity player, String channel, IOConsumer<DataOutput> writer) {
		if (canSend(player, channel)) {
			doSend(player, channel, writer);
		}
	}

	public static void send(ServerPlayerEntity player, String channel, byte[] data) {
		if (canSend(player, channel)) {
			doSend(player, channel, data);
		}
	}

	public static void send(Iterable<ServerPlayerEntity> players, String channel, IOConsumer<DataOutput> writer) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, writer));
	}

	public static void send(Iterable<ServerPlayerEntity> players, String channel, byte[] data) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, data));
	}

	public static void send(int dimension, String channel, IOConsumer<DataOutput> writer) {
		doSend(collectPlayers(p -> p.dimensionId == dimension && canSend(p, channel)), channel, writer);
	}

	public static void send(int dimension, String channel, byte[] data) {
		doSend(collectPlayers(p -> p.dimensionId == dimension && canSend(p, channel)),channel, data);
	}

	public static void send(String channel, IOConsumer<DataOutput> writer) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, writer);
	}

	public static void send(String channel, byte[] data) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, data);
	}

	public static void doSend(ServerPlayerEntity player, String channel, IOConsumer<DataOutput> writer) {
		sendPacket(player, makePacket(channel, writer));
	}

	public static void doSend(ServerPlayerEntity player, String channel, byte[] data) {
		sendPacket(player, makePacket(channel, data));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, String channel, IOConsumer<DataOutput> writer) {
		sendPacket(players, makePacket(channel, writer));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, String channel, byte[] data) {
		sendPacket(players, makePacket(channel, data));
	}

	public static void doSend(int dimension, String channel, IOConsumer<DataOutput> writer) {
		doSend(collectPlayers(p -> p.dimensionId == dimension), channel, writer);
	}

	public static void doSend(int dimension, String channel, byte[] data) {
		doSend(collectPlayers(p -> p.dimensionId == dimension),channel, data);
	}

	public static void doSend(String channel, IOConsumer<DataOutput> writer) {
		doSend(collectPlayers(p -> true), channel, writer);
	}

	public static void doSend(String channel, byte[] data) {
		doSend(collectPlayers(p -> true), channel, data);
	}

	@SuppressWarnings("unchecked") // thanks proguard
	private static Iterable<ServerPlayerEntity> collectPlayers(Predicate<ServerPlayerEntity> filter) {
		return collectPlayers(server.getPlayerManager().players, filter);
	}

	private static Iterable<ServerPlayerEntity> collectPlayers(Iterable<ServerPlayerEntity> src, Predicate<ServerPlayerEntity> filter) {
		List<ServerPlayerEntity> players = new ArrayList<>();

		for (ServerPlayerEntity player : src) {
			if (filter.test(player)) {
				players.add(player);
			}
		}

		return players;
	}

	private static Packet makePacket(String channel, IOConsumer<DataOutput> writer) {
		try {
			return new CustomPayloadPacket(channel, DataStreams.output(writer).toByteArray());
		} catch (IOException e) {
			System.out.println("error writing custom payload to channel \'" + channel + "\'");
			e.printStackTrace();

			return null;
		}
	}

	private static Packet makePacket(String channel, byte[] data) {
		return new CustomPayloadPacket(channel, data);
	}

	private static void sendPacket(ServerPlayerEntity player, Packet packet) {
		if (packet != null) {
			player.networkHandler.sendPacket(packet);
		}
	}

	private static void sendPacket(Iterable<ServerPlayerEntity> players, Packet packet) {
		if (packet != null) {
			for (ServerPlayerEntity player : players) {
				sendPacket(player, packet);
			}
		}
	}
}
