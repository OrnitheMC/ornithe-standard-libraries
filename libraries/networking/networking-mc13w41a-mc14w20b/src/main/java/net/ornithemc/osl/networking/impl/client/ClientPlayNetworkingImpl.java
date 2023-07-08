package net.ornithemc.osl.networking.impl.client;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

import net.ornithemc.osl.networking.api.PacketByteBufs;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.ByteArrayListener;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.ByteBufListener;
import net.ornithemc.osl.networking.impl.NetworkListener;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientPlayNetworkHandler;
import net.ornithemc.osl.networking.impl.interfaces.mixin.ICustomPayloadPacket;

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

	public static final Map<String, NetworkListener<ByteBufListener, ByteArrayListener>> LISTENERS = new LinkedHashMap<>();

	public static void registerListener(String channel, ByteBufListener listener) {
		registerListener(channel, listener, null);
	}

	public static void registerListener(String channel, ByteArrayListener listener) {
		registerListener(channel, null, listener);
	}

	private static void registerListener(String channel, ByteBufListener buffer, ByteArrayListener array) {
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

	public static boolean handle(Minecraft minecraft, ClientPlayNetworkHandler handler, CustomPayloadS2CPacket packet) {
		ICustomPayloadPacket p = (ICustomPayloadPacket)packet;

		String channel = p.osl$networking$getChannel();
		NetworkListener<ByteBufListener, ByteArrayListener> listener = LISTENERS.get(channel);

		if (listener != null) {
			byte[] data = p.osl$networking$getData();

			if (listener.isBuffer()) {
				return listener.buffer().handle(minecraft, handler, PacketByteBufs.make(data));
			} else {
				return listener.array().handle(minecraft, handler, data);
			}
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

	public static void send(String channel, byte[] data) {
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

	public static void doSend(String channel, byte[] data) {
		sendPacket(makePacket(channel, data));
	}

	private static Packet makePacket(String channel, Consumer<PacketByteBuf> writer) {
		return new CustomPayloadC2SPacket(channel, PacketByteBufs.make(writer));
	}

	private static Packet makePacket(String channel, PacketByteBuf data) {
		return new CustomPayloadC2SPacket(channel, data);
	}

	private static Packet makePacket(String channel, byte[] data) {
		return new CustomPayloadC2SPacket(channel, data);
	}

	private static void sendPacket(Packet packet) {
		minecraft.getNetworkHandler().sendPacket(packet);
	}
}
