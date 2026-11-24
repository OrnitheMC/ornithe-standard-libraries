package net.ornithemc.osl.networking.impl.access;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.PacketBuffer;

public interface CustomPayloadPacketAccess {

	NamespacedIdentifier osl$networking$getChannel();

	byte[] osl$networking$getDataAsBytes();

	PacketBuffer osl$networking$getDataAsBuffer();

}
