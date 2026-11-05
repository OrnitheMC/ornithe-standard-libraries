package net.ornithemc.osl.networking.impl.access;

import net.minecraft.network.packet.Packet;

public interface PacketHandlerAccess {

	boolean osl$networking$handleCustomPayload(Packet packet);

}
