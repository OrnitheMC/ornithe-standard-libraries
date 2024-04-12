package net.ornithemc.osl.networking.impl.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.Channels;
import net.ornithemc.osl.networking.api.CustomPayload;
import net.ornithemc.osl.networking.api.DataStreams;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.ByteArrayListener;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.PayloadListener;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.StreamListener;
import net.ornithemc.osl.networking.impl.CustomPayloadPacket;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientNetworkHandler;

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

	public static final Map<String, Listener> LISTENERS = new LinkedHashMap<>();

	public static <T extends CustomPayload> void registerListener(String channel, Supplier<T> initializer, PayloadListener<T> listener) {
		registerListenerImpl(channel, (minecraft, handler, data) -> {
			T payload = initializer.get();
			payload.read(DataStreams.input(data));

			return listener.handle(minecraft, handler, payload);
		});
	}

	public static void registerListener(String channel, StreamListener listener) {
		registerListenerImpl(channel, (minecraft, handler, data) -> {
			return listener.handle(minecraft, handler, DataStreams.input(data));
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

	public static boolean handle(ClientNetworkHandler handler, CustomPayloadPacket packet) {
		Listener listener = LISTENERS.get(packet.channel);

		if (listener != null) {
			try {
				return listener.handle(minecraft, handler, packet.data);
			} catch (IOException e) {
				LOGGER.warn("error handling custom payload on channel \'" + packet.channel + "\'", e);
				return true;
			}
		}

		return false;
	}

	public static boolean isPlayReady() {
		IClientNetworkHandler handler = (IClientNetworkHandler)minecraft.getNetworkHandler();
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean canSend(String channel) {
		IClientNetworkHandler handler = (IClientNetworkHandler)minecraft.getNetworkHandler();
		return handler != null && handler.osl$networking$isRegisteredServerChannel(channel);
	}

	public static void send(String channel, CustomPayload payload) {
		if (canSend(channel)) {
			doSend(channel, payload);
		}
	}

	public static void send(String channel, IOConsumer<DataOutputStream> writer) {
		if (canSend(channel)) {
			doSend(channel, writer);
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

	public static void doSend(String channel, IOConsumer<DataOutputStream> writer) {
		sendPacket(makePacket(channel, writer));
	}

	public static void doSend(String channel, byte[] data) {
		sendPacket(makePacket(channel, data));
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

	private static void sendPacket(Packet packet) {
		if (packet != null) {
			minecraft.getNetworkHandler().sendPacket(packet);
		}
	}

	private interface Listener {

		boolean handle(Minecraft minecraft, ClientNetworkHandler handler, byte[] data) throws IOException;

	}
}
