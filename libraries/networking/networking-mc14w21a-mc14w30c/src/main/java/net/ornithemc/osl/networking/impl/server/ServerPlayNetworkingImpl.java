package net.ornithemc.osl.networking.impl.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.networking.api.PacketByteBufs;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking.ByteArrayListener;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking.ByteBufListener;
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

	public static final Map<String, NetworkListener<ByteBufListener, ByteArrayListener>> LISTENERS = new LinkedHashMap<>();

	public static void registerListener(String channel, ByteBufListener listener) {
		registerListener(channel, listener, null, false);
	}

	public static void registerListener(String channel, ByteArrayListener listener) {
		registerListener(channel, null, listener, false);
	}

	public static void registerListenerAsync(String channel, ByteBufListener listener) {
		registerListener(channel, listener, null, true);
	}

	public static void registerListenerAsync(String channel, ByteArrayListener listener) {
		registerListener(channel, null, listener, true);
	}

	private static void registerListener(String channel, ByteBufListener buffer, ByteArrayListener array, boolean async) {
		LISTENERS.compute(channel, (key, value) -> {
			if (value != null) {
				throw new IllegalStateException("there is already a listener on channel \'" + channel + "\'");
			}

			return new NetworkListener<>(buffer, array, async);
		});
	}

	public static void unregisterListener(String channel) {
		LISTENERS.remove(channel);
	}

	public static boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, CustomPayloadC2SPacket packet) {
		String channel = packet.getChannel();
		NetworkListener<ByteBufListener, ByteArrayListener> listener = LISTENERS.get(channel);

		if (listener != null) {
			if (!listener.isAsync()) {
				PacketUtils.ensureOnSameThread(packet, handler, server);
			}

			byte[] data = packet.getData();

			if (listener.isBuffer()) {
				return listener.buffer().handle(server, handler, player, PacketByteBufs.make(data));
			} else {
				return listener.array().handle(server, handler, player, data);
			}
		}

		return false;
	}

	public static boolean isPlayReady(ServerPlayerEntity player) {
		IServerPlayNetworkHandler handler = (IServerPlayNetworkHandler)player.networkHandler;
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean canSend(ServerPlayerEntity player, String channel) {
		IServerPlayNetworkHandler handler = (IServerPlayNetworkHandler)player.networkHandler;
		return handler != null && handler.osl$networking$isRegisteredClientChannel(channel);
	}

	public static void send(ServerPlayerEntity player, String channel, Consumer<PacketByteBuf> writer) {
		if (canSend(player, channel)) {
			doSend(player, channel, writer);
		}
	}

	public static void send(ServerPlayerEntity player, String channel, PacketByteBuf data) {
		if (canSend(player, channel)) {
			doSend(player, channel, data);
		}
	}

	public static void send(ServerPlayerEntity player, String channel, byte[] data) {
		if (canSend(player, channel)) {
			doSend(player, channel, data);
		}
	}

	public static void send(Iterable<ServerPlayerEntity> players, String channel, Consumer<PacketByteBuf> writer) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, writer));
	}

	public static void send(Iterable<ServerPlayerEntity> players, String channel, PacketByteBuf data) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, data));
	}

	public static void send(Iterable<ServerPlayerEntity> players, String channel, byte[] data) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, data));
	}

	public static void send(int dimension, String channel, Consumer<PacketByteBuf> writer) {
		doSend(collectPlayers(p -> p.dimensionId == dimension && canSend(p, channel)), channel, writer);
	}

	public static void send(int dimension, String channel, PacketByteBuf data) {
		doSend(collectPlayers(p -> p.dimensionId == dimension && canSend(p, channel)),channel, data);
	}

	public static void send(int dimension, String channel, byte[] data) {
		doSend(collectPlayers(p -> p.dimensionId == dimension && canSend(p, channel)),channel, data);
	}

	public static void send(String channel, Consumer<PacketByteBuf> writer) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, writer);
	}

	public static void send(String channel, PacketByteBuf data) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, data);
	}

	public static void send(String channel, byte[] data) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, data);
	}

	public static void doSend(ServerPlayerEntity player, String channel, Consumer<PacketByteBuf> writer) {
		sendPacket(player, makePacket(channel, writer));
	}

	public static void doSend(ServerPlayerEntity player, String channel, PacketByteBuf data) {
		sendPacket(player, makePacket(channel, data));
	}

	public static void doSend(ServerPlayerEntity player, String channel, byte[] data) {
		sendPacket(player, makePacket(channel, data));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, String channel, Consumer<PacketByteBuf> writer) {
		sendPacket(players, makePacket(channel, writer));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, String channel, PacketByteBuf data) {
		sendPacket(players, makePacket(channel, data));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, String channel, byte[] data) {
		sendPacket(players, makePacket(channel, data));
	}

	public static void doSend(int dimension, String channel, Consumer<PacketByteBuf> writer) {
		doSend(collectPlayers(p -> p.dimensionId == dimension), channel, writer);
	}

	public static void doSend(int dimension, String channel, PacketByteBuf data) {
		doSend(collectPlayers(p -> p.dimensionId == dimension),channel, data);
	}

	public static void doSend(int dimension, String channel, byte[] data) {
		doSend(collectPlayers(p -> p.dimensionId == dimension),channel, data);
	}

	public static void doSend(String channel, Consumer<PacketByteBuf> writer) {
		doSend(collectPlayers(p -> true), channel, writer);
	}

	public static void doSend(String channel, PacketByteBuf data) {
		doSend(collectPlayers(p -> true), channel, data);
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

	private static Packet makePacket(String channel, Consumer<PacketByteBuf> writer) {
		return new CustomPayloadS2CPacket(channel, PacketByteBufs.make(writer));
	}

	private static Packet makePacket(String channel, PacketByteBuf data) {
		return new CustomPayloadS2CPacket(channel, data);
	}

	private static Packet makePacket(String channel, byte[] data) {
		return new CustomPayloadS2CPacket(channel, data);
	}

	private static void sendPacket(ServerPlayerEntity player, Packet packet) {
		player.networkHandler.sendPacket(packet);
	}

	private static void sendPacket(Iterable<ServerPlayerEntity> players, Packet packet) {
		for (ServerPlayerEntity player : players) {
			sendPacket(player, packet);
		}
	}
}
