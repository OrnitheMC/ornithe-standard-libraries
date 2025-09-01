package net.ornithemc.osl.networking.impl.server;

import java.io.IOException;
import java.nio.ByteBuffer;
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
import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.api.server.ServerPacketListener;
import net.ornithemc.osl.networking.impl.CustomPayloadPacketFactory;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.TaskRunnerAccess;

public final class ServerPlayNetworkingImpl {

	private static final Logger LOGGER = LogManager.getLogger("OSL|Server Play Networking");

	private static CustomPayloadPacketFactory packetFactory;
	private static MinecraftServer server;
	private static Thread thread;

	public static void setUpPacketFactory(CustomPayloadPacketFactory factory) {
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

	public static final Map<Channel, PacketBufferListener> LISTENERS = new LinkedHashMap<>();

	public static <T extends PacketPayload> void registerListener(Channel channel, Supplier<T> initializer, ServerPacketListener.Payload<T> listener) {
		ServerPlayNetworkingImpl.registerListener(channel, initializer, listener, false);
	}

	public static <T extends PacketPayload> void registerListenerAsync(Channel channel, Supplier<T> initializer, ServerPacketListener.Payload<T> listener) {
		ServerPlayNetworkingImpl.registerListener(channel, initializer, listener, true);
	}

	private static <T extends PacketPayload> void registerListener(Channel channel, Supplier<T> initializer, ServerPacketListener.Payload<T> listener, boolean async) {
		registerListenerImpl(channel, new PacketBufferListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, byte[] bytes) throws IOException {
				T payload = initializer.get();
				payload.read(PacketBuffers.make(bytes));

				return listener.handle(server, handler, player, payload);
			}
		});
	}

	public static void registerListener(Channel channel, ServerPacketListener.Buffer listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(Channel channel, ServerPacketListener.Buffer listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener, true);
	}

	private static void registerListener(Channel channel, ServerPacketListener.Buffer listener, boolean async) {
		registerListenerImpl(channel, new PacketBufferListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, byte[] bytes) throws IOException {
				return listener.handle(server, handler, player, PacketBuffers.make(bytes));
			}
		});
	}

	public static void registerListener(Channel channel, ServerPacketListener.Bytes listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(Channel channel, ServerPacketListener.Bytes listener) {
		ServerPlayNetworkingImpl.registerListener(channel, listener, true);
	}

	private static void registerListener(Channel channel, ServerPacketListener.Bytes listener, boolean async) {
		registerListenerImpl(channel, new PacketBufferListener() {

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

	private static void registerListenerImpl(Channel channel, PacketBufferListener listener) {
		LISTENERS.compute(channel, (key, value) -> {
			if (value != null) {
				throw new IllegalStateException("there is already a listener on channel \'" + channel + "\'");
			}

			return listener;
		});
	}

	public static void unregisterListener(Channel channel) {
		LISTENERS.remove(channel);
	}

	public static boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, Packet packet) {
		CustomPayloadPacketAccess p = (CustomPayloadPacketAccess)packet;

		Channel channel = p.osl$networking$getChannel();
		PacketBufferListener listener = LISTENERS.get(channel);

		if (listener != null) {
			if (Thread.currentThread() != thread && !listener.isAsync()) {
				return ((TaskRunnerAccess) server).osl$networking$submit(() -> handle(server, handler, player, packet));
			}

			try {
				return listener.handle(server, handler, player, p.osl$networking$getData());
			} catch (IOException e) {
				LOGGER.warn("error handling custom payload on channel \'" + channel + "\'", e);
				return true;
			}
		}

		return false;
	}

	public static boolean isPlayReady(ServerPlayerEntity player) {
		NetworkHandlerAccess handler = (NetworkHandlerAccess)player.networkHandler;
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean isPlayReady(ServerPlayerEntity player, Channel channel) {
		NetworkHandlerAccess handler = (NetworkHandlerAccess)player.networkHandler;
		return handler != null && handler.osl$networking$isPlayReady(channel);
	}

	public static void send(ServerPlayerEntity player, Channel channel, PacketPayload payload) {
		if (isPlayReady(player, channel)) {
			sendInternal(player, channel, payload);
		}
	}

	public static void send(ServerPlayerEntity player, Channel channel, IOConsumer<ByteBuffer> writer) {
		if (isPlayReady(player, channel)) {
			sendInternal(player, channel, writer);
		}
	}

	public static void send(ServerPlayerEntity player, Channel channel, ByteBuffer buffer) {
		if (isPlayReady(player, channel)) {
			sendInternal(player, channel, buffer);
		}
	}

	public static void send(ServerPlayerEntity player, Channel channel, byte[] bytes) {
		if (isPlayReady(player, channel)) {
			sendInternal(player, channel, bytes);
		}
	}

	public static void send(Iterable<ServerPlayerEntity> players, Channel channel, PacketPayload payload) {
		sendInternal(collectPlayers(players, p -> isPlayReady(p, channel)), channel, payload);
	}

	public static void send(Iterable<ServerPlayerEntity> players, Channel channel, IOConsumer<ByteBuffer> writer) {
		sendInternal(collectPlayers(players, p -> isPlayReady(p, channel)), channel, writer);
	}

	public static void send(Iterable<ServerPlayerEntity> players, Channel channel, ByteBuffer buffer) {
		sendInternal(collectPlayers(players, p -> isPlayReady(p, channel)), channel, buffer);
	}

	public static void send(Iterable<ServerPlayerEntity> players, Channel channel, byte[] bytes) {
		sendInternal(collectPlayers(players, p -> isPlayReady(p, channel)), channel, bytes);
	}

	public static void send(int dimension, Channel channel, PacketPayload payload) {
		sendInternal(collectPlayers(p -> p.dimension == dimension && isPlayReady(p, channel)), channel, payload);
	}

	public static void send(int dimension, Channel channel, IOConsumer<ByteBuffer> writer) {
		sendInternal(collectPlayers(p -> p.dimension == dimension && isPlayReady(p, channel)), channel, writer);
	}

	public static void send(int dimension, Channel channel, ByteBuffer buffer) {
		sendInternal(collectPlayers(p -> p.dimension == dimension && isPlayReady(p, channel)),channel, buffer);
	}

	public static void send(int dimension, Channel channel, byte[] bytes) {
		sendInternal(collectPlayers(p -> p.dimension == dimension && isPlayReady(p, channel)),channel, bytes);
	}

	public static void send(Channel channel, PacketPayload payload) {
		sendInternal(collectPlayers(p -> isPlayReady(p, channel)), channel, payload);
	}

	public static void send(Channel channel, IOConsumer<ByteBuffer> writer) {
		sendInternal(collectPlayers(p -> isPlayReady(p, channel)), channel, writer);
	}

	public static void send(Channel channel, ByteBuffer buffer) {
		sendInternal(collectPlayers(p -> isPlayReady(p, channel)), channel, buffer);
	}

	public static void send(Channel channel, byte[] bytes) {
		sendInternal(collectPlayers(p -> isPlayReady(p, channel)), channel, bytes);
	}

	public static void sendNoCheck(ServerPlayerEntity player, Channel channel, PacketPayload payload) {
		sendInternal(player, channel, payload);
	}

	public static void sendNoCheck(ServerPlayerEntity player, Channel channel, IOConsumer<ByteBuffer> writer) {
		sendInternal(player, channel, writer);
	}

	public static void sendNoCheck(ServerPlayerEntity player, Channel channel, ByteBuffer buffer) {
		sendInternal(player, channel, buffer);
	}

	public static void sendNoCheck(ServerPlayerEntity player, Channel channel, byte[] bytes) {
		sendInternal(player, channel, bytes);
	}

	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, Channel channel, PacketPayload payload) {
		sendInternal(players, channel, payload);
	}

	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, Channel channel, IOConsumer<ByteBuffer> writer) {
		sendInternal(players, channel, writer);
	}

	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, Channel channel, ByteBuffer buffer) {
		sendInternal(players, channel, buffer);
	}

	public static void sendNoCheck(Iterable<ServerPlayerEntity> players, Channel channel, byte[] bytes) {
		sendInternal(players, channel, bytes);
	}

	public static void sendNoCheck(int dimension, Channel channel, PacketPayload payload) {
		sendInternal(collectPlayers(p -> p.dimension == dimension), channel, payload);
	}

	public static void sendNoCheck(int dimension, Channel channel, IOConsumer<ByteBuffer> writer) {
		sendInternal(collectPlayers(p -> p.dimension == dimension), channel, writer);
	}

	public static void sendNoCheck(int dimension, Channel channel, ByteBuffer buffer) {
		sendInternal(collectPlayers(p -> p.dimension == dimension),channel, buffer);
	}

	public static void sendNoCheck(int dimension, Channel channel, byte[] bytes) {
		sendInternal(collectPlayers(p -> p.dimension == dimension),channel, bytes);
	}

	public static void sendNoCheck(Channel channel, PacketPayload payload) {
		sendInternal(allPlayers(), channel, payload);
	}

	public static void sendNoCheck(Channel channel, IOConsumer<ByteBuffer> writer) {
		sendInternal(allPlayers(), channel, writer);
	}

	public static void sendNoCheck(Channel channel, ByteBuffer buffer) {
		sendInternal(allPlayers(), channel, buffer);
	}

	public static void sendNoCheck(Channel channel, byte[] bytes) {
		sendInternal(allPlayers(), channel, bytes);
	}

	private static void sendInternal(ServerPlayerEntity player, Channel channel, PacketPayload payload) {
		sendPacket(player, makePacket(channel, payload));
	}
	
	private static void sendInternal(ServerPlayerEntity player, Channel channel, IOConsumer<ByteBuffer> writer) {
		sendPacket(player, makePacket(channel, writer));
	}
	
	private static void sendInternal(ServerPlayerEntity player, Channel channel, ByteBuffer buffer) {
		sendPacket(player, makePacket(channel, buffer));
	}
	
	private static void sendInternal(ServerPlayerEntity player, Channel channel, byte[] bytes) {
		sendPacket(player, makePacket(channel, bytes));
	}

	private static void sendInternal(Iterable<ServerPlayerEntity> players, Channel channel, PacketPayload payload) {
		sendPacket(players, makePacket(channel, payload));
	}

	private static void sendInternal(Iterable<ServerPlayerEntity> players, Channel channel, IOConsumer<ByteBuffer> writer) {
		sendPacket(players, makePacket(channel, writer));
	}

	private static void sendInternal(Iterable<ServerPlayerEntity> players, Channel channel, ByteBuffer buffer) {
		sendPacket(players, makePacket(channel, buffer));
	}

	private static void sendInternal(Iterable<ServerPlayerEntity> players, Channel channel, byte[] bytes) {
		sendPacket(players, makePacket(channel, bytes));
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

	private static Packet makePacket(Channel channel, PacketPayload payload) {
		return makePacket(channel, payload::write);
	}

	private static Packet makePacket(Channel channel, IOConsumer<ByteBuffer> writer) {
		try {
			return makePacket(channel, PacketBuffers.make(writer));
		} catch (IOException e) {
			LOGGER.warn("error writing packet payload to channel \'" + channel + "\'", e);
			return null;
		}
	}

	private static Packet makePacket(Channel channel, ByteBuffer buffer) {
		return makePacket(channel, PacketBuffers.bytes(buffer));
	}

	private static Packet makePacket(Channel channel, byte[] bytes) {
		return packetFactory.create(channel, bytes);
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

	private interface PacketBufferListener {

		boolean isAsync();

		boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, byte[] bytes) throws IOException;

	}
}
