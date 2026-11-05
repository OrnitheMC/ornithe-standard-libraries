package net.ornithemc.osl.networking.impl.access;

import net.ornithemc.osl.networking.impl.CustomPayloadPacket;

public interface PacketHandlerAccess {

	boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet);

}
