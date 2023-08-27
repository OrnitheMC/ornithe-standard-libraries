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
import net.minecraft.resource.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.minecraft.world.dimension.DimensionType;

import net.ornithemc.osl.networking.api.PacketByteBufs;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking.Listener;
import net.ornithemc.osl.networking.impl.NetworkListener;
import net.ornithemc.osl.networking.impl.interfaces.mixin.ICustomPayloadPacket;
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

	public static final Map<Identifier, NetworkListener<Listener>> LISTENERS = new LinkedHashMap<>();

	public static void registerListener(Identifier channel, Listener listener) {
		registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(Identifier channel, Listener listener) {
		registerListener(channel, listener, true);
	}

	private static void registerListener(Identifier channel, Listener listener, boolean async) {
		LISTENERS.compute(channel, (key, value) -> {
			if (value != null) {
				throw new IllegalStateException("there is already a listener on channel \'" + channel + "\'");
			}

			return new NetworkListener<>(listener, async);
		});
	}

	public static void unregisterListener(Identifier channel) {
		LISTENERS.remove(channel);
	}

	public static boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, CustomPayloadC2SPacket packet) {
		ICustomPayloadPacket p = (ICustomPayloadPacket)packet;

		Identifier channel = p.osl$networking$getChannel();
		NetworkListener<Listener> listener = LISTENERS.get(channel);

		if (listener != null) {
			if (!listener.isAsync()) {
				PacketUtils.ensureOnSameThread(packet, handler, server);
			}

			return listener.get().handle(server, handler, player, p.osl$networking$getData());
		}

		return false;
	}

	public static boolean isPlayReady(ServerPlayerEntity player) {
		IServerPlayNetworkHandler handler = (IServerPlayNetworkHandler)player.networkHandler;
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean canSend(ServerPlayerEntity player, Identifier channel) {
		IServerPlayNetworkHandler handler = (IServerPlayNetworkHandler)player.networkHandler;
		return handler != null && handler.osl$networking$isRegisteredClientChannel(channel);
	}

	public static void send(ServerPlayerEntity player, Identifier channel, Consumer<PacketByteBuf> writer) {
		if (canSend(player, channel)) {
			doSend(player, channel, writer);
		}
	}

	public static void send(ServerPlayerEntity player, Identifier channel, PacketByteBuf data) {
		if (canSend(player, channel)) {
			doSend(player, channel, data);
		}
	}

	public static void send(Iterable<ServerPlayerEntity> players, Identifier channel, Consumer<PacketByteBuf> writer) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, writer));
	}

	public static void send(Iterable<ServerPlayerEntity> players, Identifier channel, PacketByteBuf data) {
		sendPacket(collectPlayers(players, p -> canSend(p, channel)), makePacket(channel, data));
	}

	public static void send(DimensionType dimension, Identifier channel, Consumer<PacketByteBuf> writer) {
		doSend(collectPlayers(p -> p.dimension == dimension && canSend(p, channel)), channel, writer);
	}

	public static void send(DimensionType dimension, Identifier channel, PacketByteBuf data) {
		doSend(collectPlayers(p -> p.dimension == dimension && canSend(p, channel)),channel, data);
	}

	public static void send(Identifier channel, Consumer<PacketByteBuf> writer) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, writer);
	}

	public static void send(Identifier channel, PacketByteBuf data) {
		doSend(collectPlayers(p -> canSend(p, channel)), channel, data);
	}

	public static void doSend(ServerPlayerEntity player, Identifier channel, Consumer<PacketByteBuf> writer) {
		sendPacket(player, makePacket(channel, writer));
	}

	public static void doSend(ServerPlayerEntity player, Identifier channel, PacketByteBuf data) {
		sendPacket(player, makePacket(channel, data));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, Identifier channel, Consumer<PacketByteBuf> writer) {
		sendPacket(players, makePacket(channel, writer));
	}

	public static void doSend(Iterable<ServerPlayerEntity> players, Identifier channel, PacketByteBuf data) {
		sendPacket(players, makePacket(channel, data));
	}

	public static void doSend(DimensionType dimension, Identifier channel, Consumer<PacketByteBuf> writer) {
		doSend(collectPlayers(p -> p.dimension == dimension), channel, writer);
	}

	public static void doSend(DimensionType dimension, Identifier channel, PacketByteBuf data) {
		doSend(collectPlayers(p -> p.dimension == dimension),channel, data);
	}

	public static void doSend(Identifier channel, Consumer<PacketByteBuf> writer) {
		doSend(collectPlayers(p -> true), channel, writer);
	}

	public static void doSend(Identifier channel, PacketByteBuf data) {
		doSend(collectPlayers(p -> true), channel, data);
	}

	private static Iterable<ServerPlayerEntity> collectPlayers(Predicate<ServerPlayerEntity> filter) {
		return collectPlayers(server.getPlayerManager().getAll(), filter);
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

	private static Packet<?> makePacket(Identifier channel, Consumer<PacketByteBuf> writer) {
		return new CustomPayloadS2CPacket(channel, PacketByteBufs.make(writer));
	}

	private static Packet<?> makePacket(Identifier channel, PacketByteBuf data) {
		return new CustomPayloadS2CPacket(channel, data);
	}

	private static void sendPacket(ServerPlayerEntity player, Packet<?> packet) {
		player.networkHandler.sendPacket(packet);
	}

	private static void sendPacket(Iterable<ServerPlayerEntity> players, Packet<?> packet) {
		for (ServerPlayerEntity player : players) {
			sendPacket(player, packet);
		}
	}
}
