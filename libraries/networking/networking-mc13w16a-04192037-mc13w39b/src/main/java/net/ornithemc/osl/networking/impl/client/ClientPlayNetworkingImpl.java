package net.ornithemc.osl.networking.impl.client;

import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.network.packet.CustomPayloadPacket;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.DataStreams;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.ByteArrayListener;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking.StreamListener;
import net.ornithemc.osl.networking.impl.NetworkListener;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientNetworkHandler;

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

	public static final Map<String, NetworkListener<StreamListener, ByteArrayListener>> LISTENERS = new LinkedHashMap<>();

	public static void registerListener(String channel, StreamListener listener) {
		registerListener(channel, listener, null);
	}

	public static void registerListener(String channel, ByteArrayListener listener) {
		registerListener(channel, null, listener);
	}

	private static void registerListener(String channel, StreamListener buffer, ByteArrayListener array) {
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

	public static boolean handle(Minecraft minecraft, ClientNetworkHandler handler, CustomPayloadPacket packet) {
		NetworkListener<StreamListener, ByteArrayListener> listener = LISTENERS.get(packet.channel);

		if (listener != null) {
			if (listener.isStream()) {
				try {
					return listener.stream().handle(minecraft, handler, DataStreams.input(packet.data));
				} catch (IOException e) {
					System.out.println("error handling custom payload on channel \'" + packet.channel + "\'");
					e.printStackTrace();
				}
			} else {
				return listener.array().handle(minecraft, handler, packet.data);
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

	public static void send(String channel, IOConsumer<DataOutput> writer) {
		if (canSend(channel)) {
			doSend(channel, writer);
		}
	}

	public static void send(String channel, byte[] data) {
		if (canSend(channel)) {
			doSend(channel, data);
		}
	}

	public static void doSend(String channel, IOConsumer<DataOutput> writer) {
		sendPacket(makePacket(channel, writer));
	}

	public static void doSend(String channel, byte[] data) {
		sendPacket(makePacket(channel, data));
	}

	private static Packet makePacket(String channel, IOConsumer<DataOutput> writer) {
		try {
			return new CustomPayloadPacket(channel, DataStreams.output(writer).toByteArray());
		} catch (IOException e) {
			System.out.println("error writing custom payload to channel \'" + channel + "\'");
			e.printStackTrace();

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
}
