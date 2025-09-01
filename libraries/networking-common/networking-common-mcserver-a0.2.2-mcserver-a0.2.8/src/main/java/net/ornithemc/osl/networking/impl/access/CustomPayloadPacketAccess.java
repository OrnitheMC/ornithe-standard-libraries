package net.ornithemc.osl.networking.impl.access;

import net.ornithemc.osl.networking.api.Channel;

public interface CustomPayloadPacketAccess {

	Channel osl$networking$getChannel();

	byte[] osl$networking$getData();

}
