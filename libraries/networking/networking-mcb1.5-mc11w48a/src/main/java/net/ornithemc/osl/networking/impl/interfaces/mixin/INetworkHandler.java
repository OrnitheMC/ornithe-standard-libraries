package net.ornithemc.osl.networking.impl.interfaces.mixin;

import net.ornithemc.osl.networking.impl.CustomPayloadPacket;

public interface INetworkHandler {

	boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet);

}
