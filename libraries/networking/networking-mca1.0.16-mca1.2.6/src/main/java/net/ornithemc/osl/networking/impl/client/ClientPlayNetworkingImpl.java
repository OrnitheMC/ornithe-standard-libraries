package net.ornithemc.osl.networking.impl.client;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.api.client.ClientPacketListener;
import net.ornithemc.osl.networking.impl.PacketFactory;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.access.LocalClientPlayerAccess;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.TaskRunnerAccess;

public final class ClientPlayNetworkingImpl {

	private static final Logger LOGGER = LogManager.getLogger("OSL|Client Play Networking");

	private static PacketFactory packetFactory;
	private static Minecraft minecraft;
	private static Thread thread;

	public static void setUpPacketFactory(PacketFactory factory) {
		if (ClientPlayNetworkingImpl.packetFactory != null) {
			throw new IllegalStateException("tried to set up client custom payload packet factory when it was already set up!");
		}

		ClientPlayNetworkingImpl.packetFactory = factory;
	}

	public static void setUp(Minecraft minecraft) {
		if (ClientPlayNetworkingImpl.minecraft == minecraft) {
			throw new IllegalStateException("tried to set up client play networking when it was already set up!");
		}
		if (ClientPlayNetworkingImpl.packetFactory == null) {
			throw new IllegalStateException("tried to set up client play networking when no custom payload packet factory was set up!");
		}

		ClientPlayNetworkingImpl.minecraft = minecraft;
		ClientPlayNetworkingImpl.thread = Thread.currentThread();
	}

	public static void destroy(Minecraft minecraft) {
		if (ClientPlayNetworkingImpl.minecraft != minecraft) {
			throw new IllegalStateException("tried to destroy client play networking when it was not set up!");
		}

		ClientPlayNetworkingImpl.minecraft = null;
		ClientPlayNetworkingImpl.thread = null;
	}

	public static final Map<NamespacedIdentifier, ChannelListener> CHANNEL_LISTENERS = new LinkedHashMap<>();

	public static <T extends PacketPayload> void registerListener(NamespacedIdentifier channel, Supplier<T> initializer, ClientPacketListener.Payload<T> listener) {
		registerListener(channel, initializer, listener, false);
	}

	public static <T extends PacketPayload> void registerListenerAsync(NamespacedIdentifier channel, Supplier<T> initializer, ClientPacketListener.Payload<T> listener) {
		registerListener(channel, initializer, listener, true);
	}

	private static <T extends PacketPayload> void registerListener(NamespacedIdentifier channel, Supplier<T> initializer, ClientPacketListener.Payload<T> listener, boolean async) {
		registerListenerImpl(channel, new ChannelListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(Minecraft minecraft, ClientNetworkHandler handler, byte[] bytes) throws IOException {
				T payload = initializer.get();
				payload.read(PacketBuffers.wrap(bytes));

				return listener.handle(minecraft, handler, payload);
			}
		});
	}

	public static void registerListener(NamespacedIdentifier channel, ClientPacketListener.Buffer listener) {
		registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(NamespacedIdentifier channel, ClientPacketListener.Buffer listener) {
		registerListener(channel, listener, true);
	}

	private static void registerListener(NamespacedIdentifier channel, ClientPacketListener.Buffer listener, boolean async) {
		registerListenerImpl(channel, new ChannelListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(Minecraft minecraft, ClientNetworkHandler handler, byte[] bytes) throws IOException {
				return listener.handle(minecraft, handler, PacketBuffers.wrap(bytes));
			}
		});
	}

	public static void registerListener(NamespacedIdentifier channel, ClientPacketListener.Bytes listener) {
		registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(NamespacedIdentifier channel, ClientPacketListener.Bytes listener) {
		registerListener(channel, listener, true);
	}

	private static void registerListener(NamespacedIdentifier channel, ClientPacketListener.Bytes listener, boolean async) {
		registerListenerImpl(channel, new ChannelListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(Minecraft minecraft, ClientNetworkHandler handler, byte[] bytes) throws IOException {
				return listener.handle(minecraft, handler, bytes);
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

	public static boolean handlePacket(Minecraft minecraft, ClientNetworkHandler handler, Packet packet) {
		CustomPayloadPacketAccess p = (CustomPayloadPacketAccess)packet;

		NamespacedIdentifier channel = p.osl$networking$getChannel();
		ChannelListener listener = CHANNEL_LISTENERS.get(channel);

		if (listener != null) {
			byte[] data = p.osl$networking$getData();

			if (Thread.currentThread() == thread || listener.isAsync()) {
				return handlePayload(minecraft, handler, listener, channel, data);
			} else {
				return ((TaskRunnerAccess) minecraft).osl$networking$submit(() -> handlePayload(minecraft, handler, listener, channel, data));
			}
		}

		return false;
	}

	private static boolean handlePayload(Minecraft minecraft, ClientNetworkHandler handler, ChannelListener listener, NamespacedIdentifier channel, byte[] data) {
		try {
			return listener.handle(minecraft, handler, data);
		} catch (IOException e) {
			LOGGER.warn("error handling custom payload on channel \'" + channel + "\'", e);
		}

		return true;
	}

	private static ClientNetworkHandler networkHandler() {
		LocalClientPlayerAccess player = (LocalClientPlayerAccess) minecraft.player;
		return player != null ? player.osl$networking$getNetworkHandler() : null;
	}

	public static boolean isPlayReady() {
		NetworkHandlerAccess handler = (NetworkHandlerAccess) networkHandler();
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean isPlayReady(NamespacedIdentifier channel) {
		NetworkHandlerAccess handler = (NetworkHandlerAccess) networkHandler();
		return handler != null && handler.osl$networking$isPlayReady(channel);
	}

	public static void send(NamespacedIdentifier channel, PacketPayload payload) {
		if (isPlayReady(channel)) {
			sendInternal(channel, payload);
		}
	}

	public static void send(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		if (isPlayReady(channel)) {
			sendInternal(channel, writer);
		}
	}

	public static void send(NamespacedIdentifier channel, PacketBuffer buffer) {
		if (isPlayReady(channel)) {
			sendInternal(channel, buffer);
		}
	}

	public static void send(NamespacedIdentifier channel, byte[] bytes) {
		if (isPlayReady(channel)) {
			sendInternal(channel, bytes);
		}
	}

	public static void sendNoCheck(NamespacedIdentifier channel, PacketPayload payload) {
		sendInternal(channel, payload);
	}

	public static void sendNoCheck(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		sendInternal(channel, writer);
	}

	public static void sendNoCheck(NamespacedIdentifier channel, PacketBuffer buffer) {
		sendInternal(channel, buffer);
	}

	public static void sendNoCheck(NamespacedIdentifier channel, byte[] bytes) {
		sendInternal(channel, bytes);
	}

	private static void sendInternal(NamespacedIdentifier channel, PacketPayload payload) {
		try {
			sendPacket(channel, PacketBuffers.unwrap(PacketBuffers.make(payload::write)));
		} catch (IOException e) {
			LOGGER.warn("error writing packet payload to channel \'" + channel + "\'", e);
		}
	}

	private static void sendInternal(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		try {
			sendPacket(channel, PacketBuffers.unwrap(PacketBuffers.make(writer)));
		} catch (IOException e) {
			LOGGER.warn("error writing buffer to channel \'" + channel + "\'", e);
		}
	}

	private static void sendInternal(NamespacedIdentifier channel, PacketBuffer buffer) {
		sendPacket(channel, PacketBuffers.unwrap(buffer));
	}

	private static void sendInternal(NamespacedIdentifier channel, byte[] bytes) {
		sendPacket(channel, bytes);
	}

	private static void sendPacket(NamespacedIdentifier channel, byte[] data) {
		networkHandler().sendPacket(packetFactory.create(channel, data));
	}

	private interface ChannelListener {

		boolean isAsync();

		boolean handle(Minecraft minecraft, ClientNetworkHandler handler, byte[] bytes) throws IOException;

	}
}
