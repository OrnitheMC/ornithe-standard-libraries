package net.ornithemc.osl.networking.impl.access;

import net.minecraft.network.packet.CustomPayloadPacket;

public interface PacketHandlerAccess {

	boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet);

}
