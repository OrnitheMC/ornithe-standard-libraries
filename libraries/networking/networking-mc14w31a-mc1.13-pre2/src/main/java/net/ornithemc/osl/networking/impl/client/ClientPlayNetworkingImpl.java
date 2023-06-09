package net.ornithemc.osl.networking.impl.client;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

import net.ornithemc.osl.networking.api.PacketByteBufs;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.Listener;
import net.ornithemc.osl.networking.impl.NetworkListener;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientPlayNetworkHandler;

public final class ClientPlayNetworkingImpl {

	private static Minecraft minecraft;

	public static void setUp(Minecraft minecraft) {
		if (ClientPlayNetworkingImpl.minecraft == minecraft) {
			throw new IllegalStateException("tried to set up client networking when it was already set up!");
		}

		ClientPlayNetworkingImpl.minecraft = minecraft;
	}

	public static void destroy(Minecraft minecraft) {
		if (ClientPlayNetworkingImpl.minecraft != minecraft) {
			throw new IllegalStateException("tried to destroy client networking when it was not set up!");
		}

		ClientPlayNetworkingImpl.minecraft = null;
	}

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

	public static boolean handle(Minecraft minecraft, ClientPlayNetworkHandler handler, CustomPayloadS2CPacket packet) {
		String channel = packet.getChannel();
		NetworkListener<Listener> listener = LISTENERS.get(channel);

		if (listener != null) {
			if (!listener.isAsync()) {
				PacketUtils.ensureOnSameThread(packet, handler, minecraft);
			}

			return listener.get().handle(minecraft, handler, packet.getData());
		}

		return false;
	}

	public static boolean canSend(String channel) {
		IClientPlayNetworkHandler handler = (IClientPlayNetworkHandler)minecraft.getNetworkHandler();
		return handler != null && handler.osl$networking$isRegisteredServerChannel(channel);
	}

	public static void send(String channel, Consumer<PacketByteBuf> writer) {
		if (canSend(channel)) {
			doSend(channel, writer);
		}
	}

	public static void send(String channel, PacketByteBuf data) {
		if (canSend(channel)) {
			doSend(channel, data);
		}
	}

	public static void doSend(String channel, Consumer<PacketByteBuf> writer) {
		sendPacket(makePacket(channel, writer));
	}

	public static void doSend(String channel, PacketByteBuf data) {
		sendPacket(makePacket(channel, data));
	}

	private static Packet<?> makePacket(String channel, Consumer<PacketByteBuf> writer) {
		return new CustomPayloadC2SPacket(channel, PacketByteBufs.make(writer));
	}

	private static Packet<?> makePacket(String channel, PacketByteBuf data) {
		return new CustomPayloadC2SPacket(channel, data);
	}

	private static void sendPacket(Packet<?> packet) {
		minecraft.getNetworkHandler().sendPacket(packet);
	}
}
