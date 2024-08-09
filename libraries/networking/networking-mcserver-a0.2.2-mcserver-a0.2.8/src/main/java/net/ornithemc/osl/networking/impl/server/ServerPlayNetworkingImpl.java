package net.ornithemc.osl.networking.impl.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.Channels;
import net.ornithemc.osl.networking.api.CustomPayload;
import net.ornithemc.osl.networking.api.DataStreams;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking.ByteArrayListener;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking.PayloadListener;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking.StreamListener;
import net.ornithemc.osl.networking.impl.CustomPayloadPacket;
import net.ornithemc.osl.networking.impl.interfaces.mixin.INetworkHandler;

public final class ServerPlayNetworkingImpl {

	private static final Logger LOGGER = LogManager.getLogger("OSL|Server Play Networking");

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

	public static final Map<String, Listener> LISTENERS = new LinkedHashMap<>();

	public static <T extends CustomPayload> void registerListener(String channel, Supplier<T> initializer, PayloadListener<T> listener) {
		registerListenerImpl(channel, (server, handler, player, data) -> {
			T payload = initializer.get();
			payload.read(DataStreams.input(data));

			return listener.handle(server, handler, player, payload);
		});
	}

	public static void registerListener(String channel, StreamListener listener) {
		registerListenerImpl(channel, (server, handler, player, data) -> {
			return listener.handle(server, handler, player, DataStreams.input(data));
		});
	}

	public static void registerListenerRaw(String channel, ByteArrayListener listener) {
		registerListenerImpl(channel, listener::handle);
	}

	private static void registerListenerImpl(String channel, Listener listener) {
		LISTENERS.compute(channel, (key, value) -> {
			Channels.validate(channel);

			if (value != null) {
				throw new IllegalStateException("there is already a listener on channel \'" + channel + "\'");
			}

			return listener;
		});
	}

	public static void unregisterListener(String channel) {
		LISTENERS.remove(channel);
	}

	public static boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, CustomPayloadPacket packet) {
		Listener listener = LISTENERS.get(packet.channel);

		if (listener != null) {
			try {
				return listener.handle(server, handler, player, packet.data);
			} catch (IOException e) {
				LOGGER.warn("error handling custom payload on channel \'" + packet.channel + "\'", e);
				return true;
			}
		}

		return false;
	}

	public static boolean isPlayReady(ServerPlayerEntity player) {
		INetworkHandler handler = (INetworkHandler)player.networkHandler;
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean canSend(ServerPlayerEntity player, String channel) {
		INetworkHandler handler = (INetworkHandler)player.networkHandler;
		return handler != null && handler.osl$networking$isRegisteredChannel(channel);
	}

	public static void send(ServerPlayerEntity player, String channel, CustomPayload payload) {
		if (canSend(player, channel)) {
			doSend(player, channel, payload);
		}
	}

	public static void send(ServerPlayerEntity player, String channel, IOConsumer<DataOutputStream> writer) {
		if (canSend(player, channel)) {
			doSend(player, channel, writer);
		}
	}

	public static void send(ServerPlayerEntity player, String channel, byte[] data) {
		if (canSend(player, channel)) {
			doSend(player, channel, data);
		}
	}

	public static void send(Iterable<ServerPlayerEntity> players, String channel, CustomPayload payload) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, payload));
	}

	public static void send(Iterable<ServerPlayerEntity> players, String channel, IOConsumer<DataOutputStream> writer) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, writer));
	}

	public static void send(Iterable<ServerPlayerEntity> players, String channel, byte[] data) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, data));
	}

	public static void send(int dimension, String channel, CustomPayload payload) {
		doSend(collectPlayers(p -> p.dimension == dimension && canSend(p, channel)), channel, payload);
	}

	public static void send(int dimension, String channel, IOConsumer<DataOutputStream> writer) {
		doSend(collectPlayers(p -> p.dimension == dimension && canSend(p, channel)), channel, writer);
	}

	public static void send(int dimension, String channel, byte[] data) {
		doSend(collectPlayers(p -> p.dimension == dimension && canSend(p, channel)),channel, data);
	}

	public static void send(String channel, CustomPayload payload) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, payload);
	}

	public static void send(String channel, IOConsumer<DataOutputStream> writer) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, writer);
	}

	public static void send(String channel, byte[] data) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, data);
	}

	public static void doSend(ServerPlayerEntity player, String channel, CustomPayload payload) {
		sendPacket(player, makePacket(channel, payload));
	}

	public static void doSend(ServerPlayerEntity player, String channel, IOConsumer<DataOutputStream> writer) {
		sendPacket(player, makePacket(channel, writer));
	}

	public static void doSend(ServerPlayerEntity player, String channel, byte[] data) {
		sendPacket(player, makePacket(channel, data));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, String channel, CustomPayload payload) {
		sendPacket(players, makePacket(channel, payload));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, String channel, IOConsumer<DataOutputStream> writer) {
		sendPacket(players, makePacket(channel, writer));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, String channel, byte[] data) {
		sendPacket(players, makePacket(channel, data));
	}

	public static void doSend(int dimension, String channel, CustomPayload payload) {
		doSend(collectPlayers(p -> p.dimension == dimension), channel, payload);
	}

	public static void doSend(int dimension, String channel, IOConsumer<DataOutputStream> writer) {
		doSend(collectPlayers(p -> p.dimension == dimension), channel, writer);
	}

	public static void doSend(int dimension, String channel, byte[] data) {
		doSend(collectPlayers(p -> p.dimension == dimension),channel, data);
	}

	public static void doSend(String channel, CustomPayload payload) {
		doSend(collectPlayers(p -> true), channel, payload);
	}

	public static void doSend(String channel, IOConsumer<DataOutputStream> writer) {
		doSend(collectPlayers(p -> true), channel, writer);
	}

	public static void doSend(String channel, byte[] data) {
		doSend(collectPlayers(p -> true), channel, data);
	}

	@SuppressWarnings("unchecked") // thanks proguard
	private static Iterable<ServerPlayerEntity> collectPlayers(Predicate<ServerPlayerEntity> filter) {
		return collectPlayers(server.playerManager.players, filter);
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

	private static Packet makePacket(String channel, CustomPayload payload) {
		return makePacket(channel, payload::write);
	}

	private static Packet makePacket(String channel, IOConsumer<DataOutputStream> writer) {
		try {
			return new CustomPayloadPacket(channel, DataStreams.output(writer).toByteArray());
		} catch (IOException e) {
			LOGGER.warn("error writing custom payload to channel \'" + channel + "\'", e);
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

	private interface Listener {

		boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, byte[] data) throws IOException;

	}
}
