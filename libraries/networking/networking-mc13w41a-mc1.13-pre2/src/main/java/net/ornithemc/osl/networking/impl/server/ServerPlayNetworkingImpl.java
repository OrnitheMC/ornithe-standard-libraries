package net.ornithemc.osl.networking.impl.server;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.netty.buffer.Unpooled;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketUtils;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;

import net.ornithemc.osl.networking.api.server.ServerPlayNetworking.Listener;
import net.ornithemc.osl.networking.impl.NetworkListener;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IServerPlayNetworkHandler;

public final class ServerPlayNetworkingImpl {

	public static final Map<String, NetworkListener<Listener>> LISTENERS = new LinkedHashMap<>();

	public static void registerListener(String channel, Listener listener) {
		registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(String channel, Listener listener) {
		registerListener(channel, listener, true);
	}

	private static void registerListener(String channel, Listener listener, boolean async) {
		LISTENERS.compute(channel, (key, value) -> {
			if (value != null) {
				throw new IllegalStateException("there is already a listener on channel \'" + channel + "\'");
			}

			return new NetworkListener<>(listener, async);
		});
	}

	public static void unregisterListener(String channel) {
		LISTENERS.remove(channel);
	}

	public static boolean handle(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, CustomPayloadC2SPacket packet) {
		String channel = packet.getChannel();
		NetworkListener<Listener> listener = LISTENERS.get(channel);

		if (listener != null) {
			if (!listener.isAsync()) {
				PacketUtils.ensureOnSameThread(packet, handler, server);
			}

			return listener.get().handle(server, handler, player, packet.getData());
		}

		return false;
	}

	public static boolean canSend(ServerPlayerEntity player, String channel) {
		return ((IServerPlayNetworkHandler)player.networkHandler).osl$networking$isRegisteredClientChannel(channel);
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

	public static void send(Collection<ServerPlayerEntity> players, String channel, Consumer<PacketByteBuf> writer) {
		PacketByteBuf data = null;

		for (ServerPlayerEntity player : players) {
			if (canSend(player, channel)) {
				if (data == null) {
					writer.accept(data = new PacketByteBuf(Unpooled.buffer()));
				}

				doSend(player, channel, writer);
			}
		}
	}

	public static void send(Collection<ServerPlayerEntity> players, String channel, PacketByteBuf data) {
		for (ServerPlayerEntity player : players) {
			if (canSend(player, channel)) {
				doSend(player, channel, data);
			}
		}
	}

	public static void send(ServerWorld world, String channel, Consumer<PacketByteBuf> writer) {
		send(world.getPlayers(ServerPlayerEntity.class, p -> true), channel, writer);
	}

	public static void send(ServerWorld world, String channel, PacketByteBuf data) {
		send(world.getPlayers(ServerPlayerEntity.class, p -> true), channel, data);
	}

	public static void send(MinecraftServer server, String channel, Consumer<PacketByteBuf> writer) {
		send(server.getPlayerManager().getAll(), channel, writer);
	}

	public static void send(MinecraftServer server, String channel, PacketByteBuf data) {
		send(server.getPlayerManager().getAll(), channel, data);
	}

	public static void doSend(ServerPlayerEntity player, String channel, Consumer<PacketByteBuf> writer) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		writer.accept(data);

		doSend(player, channel, data);
	}

	public static void doSend(ServerPlayerEntity player, String channel, PacketByteBuf data) {
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(channel, data));
	}
}
