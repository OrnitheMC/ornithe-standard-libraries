package net.ornithemc.osl.networking.impl.client;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.Channels;
import net.ornithemc.osl.networking.api.CustomPayload;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.ByteArrayListener;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.ByteBufListener;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.PayloadListener;
import net.ornithemc.osl.networking.impl.NetworkListener;
import net.ornithemc.osl.networking.impl.interfaces.mixin.INetworkHandler;

public final class ClientPlayNetworkingImpl {

	private static final Logger LOGGER = LogManager.getLogger("OSL|Client Play Networking");

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

	public static <T extends CustomPayload> void registerListener(String channel, Supplier<T> initializer, PayloadListener<T> listener) {
		registerListener(channel, initializer, listener, false);
	}

	public static <T extends CustomPayload> void registerListenerAsync(String channel, Supplier<T> initializer, PayloadListener<T> listener) {
		registerListener(channel, initializer, listener, true);
	}

	private static <T extends CustomPayload> void registerListener(String channel, Supplier<T> initializer, PayloadListener<T> listener, boolean async) {
		registerListenerImpl(channel, (minecraft, handler, data) -> {
			T payload = initializer.get();
			payload.read(PacketBuffers.make(data));

			return listener.handle(minecraft, handler, payload);
		}, async);
	}

	public static void registerListener(String channel, ByteBufListener listener) {
		registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(String channel, ByteBufListener listener) {
		registerListener(channel, listener, true);
	}

	private static void registerListener(String channel, ByteBufListener listener, boolean async) {
		registerListenerImpl(channel, (minecraft, handler, data) -> {
			return listener.handle(minecraft, handler, PacketBuffers.make(data));
		}, async);
	}

	public static void registerListenerRaw(String channel, ByteArrayListener listener) {
		registerListenerRaw(channel, listener, false);
	}

	public static void registerListenerRawAsync(String channel, ByteArrayListener listener) {
		registerListenerRaw(channel, listener, true);
	}

	private static void registerListenerRaw(String channel, ByteArrayListener listener, boolean async) {
		registerListenerImpl(channel, listener::handle, async);
	}

	private static void registerListenerImpl(String channel, Listener listener, boolean async) {
		LISTENERS.compute(channel, (key, value) -> {
			Channels.validate(channel);

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

			try {
				return listener.get().handle(minecraft, handler, packet.getData());
			} catch (IOException e) {
				LOGGER.warn("error handling custom payload on channel \'" + channel + "\'", e);
				return true;
			}
		}

		return false;
	}

	public static boolean isPlayReady() {
		INetworkHandler handler = (INetworkHandler)minecraft.getNetworkHandler();
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean canSend(String channel) {
		INetworkHandler handler = (INetworkHandler)minecraft.getNetworkHandler();
		return handler != null && handler.osl$networking$isRegisteredChannel(channel);
	}

	public static void send(String channel, CustomPayload payload) {
		if (canSend(channel)) {
			doSend(channel, payload);
		}
	}

	public static void send(String channel, IOConsumer<PacketByteBuf> writer) {
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

	public static void doSend(String channel, CustomPayload payload) {
		sendPacket(makePacket(channel, payload));
	}

	public static void doSend(String channel, IOConsumer<PacketByteBuf> writer) {
		sendPacket(makePacket(channel, writer));
	}

	public static void doSend(String channel, PacketByteBuf data) {
		sendPacket(makePacket(channel, data));
	}

	public static void doSend(String channel, byte[] data) {
		sendPacket(makePacket(channel, data));
	}

	private static Packet makePacket(String channel, CustomPayload payload) {
		return makePacket(channel, payload::write);
	}

	private static Packet makePacket(String channel, IOConsumer<PacketByteBuf> writer) {
		try {
			return new CustomPayloadC2SPacket(channel, PacketBuffers.make(writer));
		} catch (IOException e) {
			LOGGER.warn("error writing custom payload to channel \'" + channel + "\'", e);
			return null;
		}
	}

	private static Packet makePacket(String channel, PacketByteBuf data) {
		return new CustomPayloadC2SPacket(channel, data);
	}

	private static Packet makePacket(String channel, byte[] data) {
		return new CustomPayloadC2SPacket(channel, data);
	}

	private static void sendPacket(Packet packet) {
		if (packet != null) {
			minecraft.getNetworkHandler().sendPacket(packet);
		}
	}

	private interface Listener {

		boolean handle(Minecraft minecraft, ClientPlayNetworkHandler handler, byte[] data) throws IOException;

	}
}
