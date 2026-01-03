package net.ornithemc.osl.networking.impl;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;

public final class Connections {

	public static boolean checkAsyncHandling(Packet packet, PacketHandler listener) {
		boolean handleAsync = packet instanceof CustomPayloadPacketAccess
							&& listener instanceof NetworkHandlerAccess
							&& ((NetworkHandlerAccess) listener).osl$networking$canRunOffMainThread();

		if (handleAsync) {
			packet.handle(listener);
		}

		return handleAsync;
	}
}
