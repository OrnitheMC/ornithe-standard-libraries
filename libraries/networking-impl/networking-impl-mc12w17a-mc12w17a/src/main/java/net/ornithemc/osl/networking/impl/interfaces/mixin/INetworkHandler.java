package net.ornithemc.osl.networking.impl.interfaces.mixin;

import net.minecraft.network.packet.CustomPayloadPacket;

public interface INetworkHandler {

	boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet);

}
