package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

import net.minecraft.network.packet.CustomPayloadPacket;

public interface IClientNetworkHandler {

	boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet);

	boolean osl$networking$isPlayReady();

	void osl$networking$registerServerChannels(Set<String> channels);

	boolean osl$networking$isRegisteredServerChannel(String channel);

}
