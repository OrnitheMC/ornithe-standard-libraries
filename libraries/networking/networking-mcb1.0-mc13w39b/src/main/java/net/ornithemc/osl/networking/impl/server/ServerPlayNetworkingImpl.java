package net.ornithemc.osl.networking.impl.server;

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

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.api.server.ServerPacketListener;
import net.ornithemc.osl.networking.impl.PacketFactory;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.TaskRunnerAccess;

public final class ServerPlayNetworkingImpl {

	private static final Logger LOGGER = LogManager.getLogger("OSL|Server Play Networking");

	private static PacketFactory packetFactory;
	private static MinecraftServer server;
	private static Thread thread;

	public static void setUpPacketFactory(PacketFactory factory) {
		if (ServerPlayNetworkingImpl.packetFactory != null) {
			throw new IllegalStateException("tried to set up server custom payload packet factory when it was already set up!");
		}

		ServerPlayNetworkingImpl.packetFactory = factory;
	}

	public static void setUp(MinecraftServer server) {
		if (ServerPlayNetworkingImpl.server == server) {
			throw new IllegalStateException("tried to set up server play networking when it was already set up!");
		}
		if (ServerPlayNetworkingImpl.packetFactory == null) {
			throw new IllegalStateException("tried to set up server play networking when no custom payload packet factory was set up!");
		}

		ServerPlayNetworkingImpl.server = server;
		ServerPlayNetworkingImpl.thread = Thread.currentThread();
	}

	public static void destroy(MinecraftServer server) {
		if (ServerPlayNetworkingImpl.server != server) {
			throw new IllegalStateException("tried to destroy server play networking when it was not set up!");
		}

		ServerPlayNetworkingImpl.server = null;
		ServerPlayNetworkingImpl.thread = null;
	}

	public static final Map<NamespacedIdentifier, ChannelListener> CHANNEL_LISTENERS = new LinkedHashMap<>();

	public static <T extends PacketPayload> void registerListener(NamespacedIdentifier channel, Supplier<T> initializer, ServerPacketListener.Payload<T> listener) {
		ServerPlayNetworkingImpl.registerListener(channel, initializer, listener, false);
	}

	public static <T extends PacketPayload> void registerListenerAsync(NamespacedIdentifier channel, Supplier<T> initializer, ServerPacketListener.Payload<T> listener) {
		ServerPlayNetworkingImpl.registerListener(channel, initializer, listener, true);
	}

	private static <T extends PacketPayload> void registerListener(NamespacedIdentifier channel, Supplier<T> initializer, ServerPacketListener.Payload<T> listener, boolean async) {
		registerListenerImpl(channel, new ChannelListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, byte[] bytes) throws IOException {
				T payload = initializer.get();
				payload.read(PacketBuffers.wrap(bytes));

				return listener.handle(server, handler, player, payload);
			}
		});
	}

	public static void registerListener(NamespacedIdentifier channel, ServerPacketListener.Buffer listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(NamespacedIdentifier channel, ServerPacketListener.Buffer listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener, true);
	}

	private static void registerListener(NamespacedIdentifier channel, ServerPacketListener.Buffer listener, boolean async) {
		registerListenerImpl(channel, new ChannelListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, byte[] bytes) throws IOException {
				return listener.handle(server, handler, player, PacketBuffers.wrap(bytes));
			}
		});
	}

	public static void registerListener(NamespacedIdentifier channel, ServerPacketListener.Bytes listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(NamespacedIdentifier channel, ServerPacketListener.Bytes listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener, true);
	}

	private static void registerListener(NamespacedIdentifier channel, ServerPacketListener.Bytes listener, boolean async) {
		registerListenerImpl(channel, new ChannelListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, byte[] bytes) throws IOException {
				return listener.handle(server, handler, player, bytes);
			}
		});
	}

	private static void registerListenerImpl(NamespacedIdentifier channel, ChannelListener listener) {
		CHANNEL_LISTENERS.compute(channel, (key, value) -> {
			if (value != null) {
				throw new IllegalStateException("there is already a listener on channel \'" + channel + "\'");
			}

			return listener;
		});
	}

	public static void unregisterListener(NamespacedIdentifier channel) {
		CHANNEL_LISTENERS.remove(channel);
	}

	public static boolean handlePacket(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, Packet packet) {
		CustomPayloadPacketAccess p = (CustomPayloadPacketAccess)packet;

		NamespacedIdentifier channel = p.osl$networking$getChannel();
		ChannelListener listener = CHANNEL_LISTENERS.get(channel);

		if (listener != null) {
			byte[] data = p.osl$networking$getData();

			if (Thread.currentThread() == thread || listener.isAsync()) {
				return handlePayload(server, handler, player, listener, channel, data);
			} else {
				return ((TaskRunnerAccess) server).osl$networking$submit(() -> handlePayload(server, handler, player, listener, channel, data));
			}
		}

		return false;
	}

	private static boolean handlePayload(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, ChannelListener listener, NamespacedIdentifier channel, byte[] data) {
		try {
			return listener.handle(server, handler, player, data);
		} catch (IOException e) {
			LOGGER.warn("error handling custom payload on channel \'" + channel + "\'", e);
		}

		return true;
	}

	public static boolean isPlayReady(ServerPlayerEntity player) {
		NetworkHandlerAccess handler = (NetworkHandlerAccess)player.networkHandler;
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean isPlayReady(ServerPlayerEntity player, NamespacedIdentifier channel) {
		NetworkHandlerAccess handler = (NetworkHandlerAccess)player.networkHandler;
		return handler != null && handler.osl$networking$isPlayReady(channel);
	}

	public static void send(ServerPlayerEntity player, NamespacedIdentifier channel, PacketPayload payload) {
		if (isPlayReady(player, channel)) {
			sendInternal(player, channel, payload);
		}
	}

	public static void send(ServerPlayerEntity player, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		if (isPlayReady(player, channel)) {
			sendInternal(player, channel, writer);
		}
	}

	public static void send(ServerPlayerEntity player, NamespacedIdentifier channel, PacketBuffer buffer) {
		if (isPlayReady(player, channel)) {
			sendInternal(player, channel, buffer);
		}
	}

	public static void send(ServerPlayerEntity player, NamespacedIdentifier channel, byte[] bytes) {
		if (isPlayReady(player, channel)) {
			sendInternal(player, channel, bytes);
		}
	}

	public static void send(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketPayload payload) {
		sendInternal(collectPlayers(players, p -> isPlayReady(p, channel)), channel, payload);
	}

	public static void send(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		sendInternal(collectPlayers(players, p -> isPlayReady(p, channel)), channel, writer);
	}

	public static void send(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketBuffer buffer) {
		sendInternal(collectPlayers(players, p -> isPlayReady(p, channel)), channel, buffer);
	}

	public static void send(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, byte[] bytes) {
		sendInternal(collectPlayers(players, p -> isPlayReady(p, channel)), channel, bytes);
	}

	public static void send(int dimension, NamespacedIdentifier channel, PacketPayload payload) {
		sendInternal(collectPlayers(p -> p.dimension == dimension && isPlayReady(p, channel)), channel, payload);
	}

	public static void send(int dimension, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		sendInternal(collectPlayers(p -> p.dimension == dimension && isPlayReady(p, channel)), channel, writer);
	}

	public static void send(int dimension, NamespacedIdentifier channel, PacketBuffer buffer) {
		sendInternal(collectPlayers(p -> p.dimension == dimension && isPlayReady(p, channel)),channel, buffer);
	}

	public static void send(int dimension, NamespacedIdentifier channel, byte[] bytes) {
		sendInternal(collectPlayers(p -> p.dimension == dimension && isPlayReady(p, channel)),channel, bytes);
	}

	public static void send(NamespacedIdentifier channel, PacketPayload payload) {
		sendInternal(collectPlayers(p -> isPlayReady(p, channel)), channel, payload);
	}

	public static void send(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		sendInternal(collectPlayers(p -> isPlayReady(p, channel)), channel, writer);
	}

	public static void send(NamespacedIdentifier channel, PacketBuffer buffer) {
		sendInternal(collectPlayers(p -> isPlayReady(p, channel)), channel, buffer);
	}

	public static void send(NamespacedIdentifier channel, byte[] bytes) {
		sendInternal(collectPlayers(p -> isPlayReady(p, channel)), channel, bytes);
	}

	public static void sendNoCheck(ServerPlayerEntity player, NamespacedIdentifier channel, PacketPayload payload) {
		sendInternal(player, channel, payload);
	}

	public static void sendNoCheck(ServerPlayerEntity player, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		sendInternal(player, channel, writer);
	}

	public static void sendNoCheck(ServerPlayerEntity player, NamespacedIdentifier channel, PacketBuffer buffer) {
		sendInternal(player, channel, buffer);
	}

	public static void sendNoCheck(ServerPlayerEntity player, NamespacedIdentifier channel, byte[] bytes) {
		sendInternal(player, channel, bytes);
	}

	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketPayload payload) {
		sendInternal(players, channel, payload);
	}

	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		sendInternal(players, channel, writer);
	}

	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketBuffer buffer) {
		sendInternal(players, channel, buffer);
	}

	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, byte[] bytes) {
		sendInternal(players, channel, bytes);
	}

	public static void sendNoCheck(int dimension, NamespacedIdentifier channel, PacketPayload payload) {
		sendInternal(collectPlayers(p -> p.dimension == dimension), channel, payload);
	}

	public static void sendNoCheck(int dimension, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		sendInternal(collectPlayers(p -> p.dimension == dimension), channel, writer);
	}

	public static void sendNoCheck(int dimension, NamespacedIdentifier channel, PacketBuffer buffer) {
		sendInternal(collectPlayers(p -> p.dimension == dimension),channel, buffer);
	}

	public static void sendNoCheck(int dimension, NamespacedIdentifier channel, byte[] bytes) {
		sendInternal(collectPlayers(p -> p.dimension == dimension),channel, bytes);
	}

	public static void sendNoCheck(NamespacedIdentifier channel, PacketPayload payload) {
		sendInternal(allPlayers(), channel, payload);
	}

	public static void sendNoCheck(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		sendInternal(allPlayers(), channel, writer);
	}

	public static void sendNoCheck(NamespacedIdentifier channel, PacketBuffer buffer) {
		sendInternal(allPlayers(), channel, buffer);
	}

	public static void sendNoCheck(NamespacedIdentifier channel, byte[] bytes) {
		sendInternal(allPlayers(), channel, bytes);
	}

	private static void sendInternal(ServerPlayerEntity player, NamespacedIdentifier channel, PacketPayload payload) {
		try {
			sendPacket(player, channel, PacketBuffers.unwrap(PacketBuffers.make(payload::write)));
		} catch (IOException e) {
			LOGGER.warn("error writing packet payload to channel \'" + channel + "\'", e);
		}
	}
	
	private static void sendInternal(ServerPlayerEntity player, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		try {
			sendPacket(player, channel, PacketBuffers.unwrap(PacketBuffers.make(writer)));
		} catch (IOException e) {
			LOGGER.warn("error writing buffer to channel \'" + channel + "\'", e);
		}
	}
	
	private static void sendInternal(ServerPlayerEntity player, NamespacedIdentifier channel, PacketBuffer buffer) {
		sendPacket(player, channel, PacketBuffers.unwrap(buffer));
	}
	
	private static void sendInternal(ServerPlayerEntity player, NamespacedIdentifier channel, byte[] bytes) {
		sendPacket(player, channel, bytes);
	}

	private static void sendInternal(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketPayload payload) {
		try {
			sendPacket(players, channel, PacketBuffers.unwrap(PacketBuffers.make(payload::write)));
		} catch (IOException e) {
			LOGGER.warn("error writing packet payload to channel \'" + channel + "\'", e);
		}
	}

	private static void sendInternal(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		try {
			sendPacket(players, channel, PacketBuffers.unwrap(PacketBuffers.make(writer)));
		} catch (IOException e) {
			LOGGER.warn("error writing buffer to channel \'" + channel + "\'", e);
		}
	}

	private static void sendInternal(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, PacketBuffer buffer) {
		sendPacket(players, channel, PacketBuffers.unwrap(buffer));
	}

	private static void sendInternal(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, byte[] bytes) {
		sendPacket(players, channel, bytes);
	}

	private static Iterable<ServerPlayerEntity> allPlayers() {
		return server.getPlayerManager().players;
	}

	private static Iterable<ServerPlayerEntity> collectPlayers(Predicate<ServerPlayerEntity> filter) {
		return collectPlayers(allPlayers(), filter);
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

	private static void sendPacket(ServerPlayerEntity player, NamespacedIdentifier channel, byte[] data) {
		player.networkHandler.sendPacket(packetFactory.create(channel, data));
	}

	private static void sendPacket(Iterable<ServerPlayerEntity> players, NamespacedIdentifier channel, byte[] data) {
		Packet packet = packetFactory.create(channel, data);

		for (ServerPlayerEntity player : players) {
			player.networkHandler.sendPacket(packet);
		}
	}

	private interface ChannelListener {

		boolean isAsync();

		boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, byte[] bytes) throws IOException;

	}
}
