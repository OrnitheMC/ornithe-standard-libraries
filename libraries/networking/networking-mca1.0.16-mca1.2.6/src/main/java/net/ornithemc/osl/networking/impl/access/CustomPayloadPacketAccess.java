package net.ornithemc.osl.networking.impl.access;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface CustomPayloadPacketAccess {

	NamespacedIdentifier osl$networking$getChannel();

	byte[] osl$networking$getData();

}
