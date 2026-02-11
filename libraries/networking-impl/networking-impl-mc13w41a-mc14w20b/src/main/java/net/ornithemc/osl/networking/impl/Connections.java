package net.ornithemc.osl.networking.impl;

import net.minecraft.network.handler.PacketHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;

public final class Connections {

	public static boolean handleAsyncPacket(Packet packet, PacketHandler listener) {
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
				packet.handle(listener);
			} catch (NotOnMainThreadException e) {
				handleAsync = false;
			}
		}

		return handleAsync;
	}
}
