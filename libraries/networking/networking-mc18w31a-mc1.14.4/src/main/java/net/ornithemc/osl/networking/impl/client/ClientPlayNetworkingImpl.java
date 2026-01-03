package net.ornithemc.osl.networking.impl.client;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.api.client.ClientPacketListener;
import net.ornithemc.osl.networking.impl.ChannelRegistryImpl;
import net.ornithemc.osl.networking.impl.ChannelSettings;
import net.ornithemc.osl.networking.impl.NotOnMainThreadException;
import net.ornithemc.osl.networking.impl.PacketFactory;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
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
		registerListenerInternal(channel, (context, buffer) -> {
			T payload = initializer.get();
			payload.read(buffer);

			listener.handle(context, payload);
		});
	}

	public static void registerListener(NamespacedIdentifier channel, ClientPacketListener.Buffer listener) {
		registerListenerInternal(channel, listener::handle);
	}

	public static void registerListener(NamespacedIdentifier channel, ClientPacketListener.Bytes listener) {
		registerListenerInternal(channel, (context, buffer) -> listener.handle(context, buffer.readByteArray()));
	}

	private static void registerListenerInternal(NamespacedIdentifier channel, ChannelListener listener) {
		ChannelSettings settings = ChannelRegistryImpl.getSettings(channel);

		if (settings == null || !settings.isClientbound()) {
			throw new IllegalArgumentException("channel \'" + channel + "\' is not client-bound - did you register it with the wrong settings?");
		}

		CHANNEL_LISTENERS.compute(channel, (key, value) -> {
			if (value != null) {
				throw new IllegalArgumentException("there is already a listener on channel \'" + channel + "\'");
			}

			return listener;
		});
	}

	public static void unregisterListener(NamespacedIdentifier channel) {
		CHANNEL_LISTENERS.remove(channel);
	}

	public static boolean handlePacket(Minecraft minecraft, ClientPlayNetworkHandler handler, Packet<?> packet) {
		CustomPayloadPacketAccess p = (CustomPayloadPacketAccess)packet;

		NamespacedIdentifier channel = p.osl$networking$getChannel();
		ChannelListener listener = CHANNEL_LISTENERS.get(channel);

		if (listener != null) {
			ChannelListener.Context ctx = new ChannelListener.Context();
			PacketBuffer data = p.osl$networking$getData();

			try {
				handlePayload(channel, listener, ctx, data);
			} catch (NotOnMainThreadException e) {
				((TaskRunnerAccess) minecraft).osl$networking$submit(() -> handlePayload(channel, listener, ctx, data));
			}

			return true;
		}

		return false;
	}

	private static void handlePayload(NamespacedIdentifier channel, ChannelListener listener, ChannelListener.Context ctx, PacketBuffer data) {
		try {
			listener.handle(ctx, data);
		} catch (IOException e) {
			LOGGER.warn("error handling custom payload on channel \'" + channel + "\'", e);
		}
	}

	public static boolean isPlayReady() {
		NetworkHandlerAccess handler = (NetworkHandlerAccess)minecraft.getNetworkHandler();
		return handler != null && handler.osl$networking$isPlayReady();
	}

	public static boolean isPlayReady(NamespacedIdentifier channel) {
		NetworkHandlerAccess handler = (NetworkHandlerAccess)minecraft.getNetworkHandler();
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
			sendPacket(channel, PacketBuffers.make(payload::write));
		} catch (IOException e) {
			LOGGER.warn("error writing packet payload to channel \'" + channel + "\'", e);
		}
	}

	private static void sendInternal(NamespacedIdentifier channel, IOConsumer<PacketBuffer> writer) {
		try {
			sendPacket(channel, PacketBuffers.make(writer));
		} catch (IOException e) {
			LOGGER.warn("error writing buffer to channel \'" + channel + "\'", e);
		}
	}

	private static void sendInternal(NamespacedIdentifier channel, PacketBuffer buffer) {
		sendPacket(channel, buffer);
	}

	private static void sendInternal(NamespacedIdentifier channel, byte[] bytes) {
		sendPacket(channel, PacketBuffers.wrap(bytes));
	}

	private static void sendPacket(NamespacedIdentifier channel, PacketBuffer data) {
		ChannelSettings settings = ChannelRegistryImpl.getSettings(channel);

		if (settings != null && settings.isServerbound()) {
			minecraft.getNetworkHandler().sendPacket(packetFactory.create(channel, data));
		}
	}

	@FunctionalInterface
	private interface ChannelListener {

		void handle(Context context, PacketBuffer buffer) throws IOException;

		class Context implements ClientPacketListener.Context {

			@Override
			public Minecraft minecraft() {
				return minecraft;
			}

			@Override
			public ClientPlayNetworkHandler networkHandler() {
				return minecraft.getNetworkHandler();
			}

			@Override
			public void ensureOnMainThread() {
				if (Thread.currentThread() != thread) {
					throw NotOnMainThreadException.INSTANCE;
				}
			}
		}
	}
}
