package net.ornithemc.osl.networking.impl.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.api.client.ClientPacketListener;
import net.ornithemc.osl.networking.impl.CustomPayloadPacketFactory;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.TaskRunnerAccess;

public final class ClientPlayNetworkingImpl {

	private static final Logger LOGGER = LogManager.getLogger("OSL|Client Play Networking");

	private static CustomPayloadPacketFactory packetFactory;
	private static Minecraft minecraft;
	private static Thread thread;

	public static void setUpPacketFactory(CustomPayloadPacketFactory factory) {
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

	public static final Map<Channel, BytesListener> LISTENERS = new LinkedHashMap<>();

	public static <T extends PacketPayload> void registerListener(Channel channel, Supplier<T> initializer, ClientPacketListener.Payload<T> listener) {
		registerListener(channel, initializer, listener, false);
	}

	public static <T extends PacketPayload> void registerListenerAsync(Channel channel, Supplier<T> initializer, ClientPacketListener.Payload<T> listener) {
		registerListener(channel, initializer, listener, true);
	}

	private static <T extends PacketPayload> void registerListener(Channel channel, Supplier<T> initializer, ClientPacketListener.Payload<T> listener, boolean async) {
		registerListenerImpl(channel, new BytesListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(Minecraft minecraft, ClientNetworkHandler handler, byte[] bytes) throws IOException {
				T payload = initializer.get();
				payload.read(PacketBuffers.make(bytes));

				return listener.handle(minecraft, handler, payload);
			}
		});
	}

	public static void registerListener(Channel channel, ClientPacketListener.Buffer listener) {
		registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(Channel channel, ClientPacketListener.Buffer listener) {
		registerListener(channel, listener, true);
	}

	private static void registerListener(Channel channel, ClientPacketListener.Buffer listener, boolean async) {
		registerListenerImpl(channel, new BytesListener() {

			@Override
			public boolean isAsync() {
				return async;
			}

			@Override
			public boolean handle(Minecraft minecraft, ClientNetworkHandler handler, byte[] bytes) throws IOException {
				return listener.handle(minecraft, handler, PacketBuffers.make(bytes));
			}
		});
	}

	public static void registerListener(Channel channel, ClientPacketListener.Bytes listener) {
		registerListener(channel, listener, false);
	}

	public static void registerListenerAsync(Channel channel, ClientPacketListener.Bytes listener) {
		registerListener(channel, listener, true);
	}

	private static void registerListener(Channel channel, ClientPacketListener.Bytes listener, boolean async) {
		registerListenerImpl(channel, new BytesListener() {

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

	private static void registerListenerImpl(Channel channel, BytesListener listener) {
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

	public static boolean handle(Minecraft minecraft, ClientNetworkHandler handler, Packet packet) {
		CustomPayloadPacketAccess p = (CustomPayloadPacketAccess)packet;

		Channel channel = p.osl$networking$getChannel();
		BytesListener listener = LISTENERS.get(channel);

		if (listener != null) {
			if (Thread.currentThread() != thread && !listener.isAsync()) {
				return ((TaskRunnerAccess) minecraft).osl$networking$submit(() -> handle(minecraft, handler, packet));
			}

			try {
				return listener.handle(minecraft, handler, p.osl$networking$getData());
			} catch (IOException e) {
				LOGGER.warn("error handling custom payload on channel \'" + channel + "\'", e);
				return true;
			}
		}

		return false;
	}

	public static boolean isPlayReady() {
		NetworkHandlerAccess handler = (NetworkHandlerAccess)minecraft.getNetworkHandler();
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean isPlayReady(Channel channel) {
		NetworkHandlerAccess handler = (NetworkHandlerAccess)minecraft.getNetworkHandler();
		return handler != null && handler.osl$networking$isPlayReady(channel);
	}

	public static void send(Channel channel, PacketPayload payload) {
		if (isPlayReady(channel)) {
			sendInternal(channel, payload);
		}
	}

	public static void send(Channel channel, IOConsumer<ByteBuffer> writer) {
		if (isPlayReady(channel)) {
			sendInternal(channel, writer);
		}
	}

	public static void send(Channel channel, ByteBuffer buffer) {
		if (isPlayReady(channel)) {
			sendInternal(channel, buffer);
		}
	}

	public static void send(Channel channel, byte[] bytes) {
		if (isPlayReady(channel)) {
			sendInternal(channel, bytes);
		}
	}

	public static void sendNoCheck(Channel channel, PacketPayload payload) {
		sendInternal(channel, payload);
	}

	public static void sendNoCheck(Channel channel, IOConsumer<ByteBuffer> writer) {
		sendInternal(channel, writer);
	}

	public static void sendNoCheck(Channel channel, ByteBuffer buffer) {
		sendInternal(channel, buffer);
	}

	public static void sendNoCheck(Channel channel, byte[] bytes) {
		sendInternal(channel, bytes);
	}

	private static void sendInternal(Channel channel, PacketPayload payload) {
		sendPacket(makePacket(channel, payload));
	}

	private static void sendInternal(Channel channel, IOConsumer<ByteBuffer> writer) {
		sendPacket(makePacket(channel, writer));
	}

	private static void sendInternal(Channel channel, ByteBuffer buffer) {
		sendPacket(makePacket(channel, buffer));
	}

	private static void sendInternal(Channel channel, byte[] bytes) {
		sendPacket(makePacket(channel, bytes));
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

	private static void sendPacket(Packet packet) {
		if (packet != null) {
			minecraft.getNetworkHandler().sendPacket(packet);
		}
	}

	private interface BytesListener {

		boolean isAsync();

		boolean handle(Minecraft minecraft, ClientNetworkHandler handler, byte[] bytes) throws IOException;

	}
}
