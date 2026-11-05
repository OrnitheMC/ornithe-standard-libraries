package net.ornithemc.osl.networking.impl;

import java.util.function.BiConsumer;

import net.minecraft.network.handler.PacketHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;

public final class Connections {

	// this generics + BiConsumer BS is necessary because Packet changes from a class to an interface
	public static <P extends Packet, L extends PacketHandler> boolean handleAsyncPacket(P packet, L listener, BiConsumer<L, P> handler) {
		if (!(packet instanceof CustomPayloadPacketAccess)) {
			return false;
		}
		if (!(listener instanceof NetworkHandlerAccess)) {
			return false;
		}

		CustomPayloadPacketAccess p = (CustomPayloadPacketAccess) packet;
		NamespacedIdentifier channel = p.osl$networking$getChannel();

		boolean handleAsync = ChannelRegistryImpl.contains(channel);

		if (handleAsync) {
			try {
				handler.accept(listener, packet);
			} catch (NotOnMainThreadException e) {
				handleAsync = false;
			}
		}

		return handleAsync;
	}
}
