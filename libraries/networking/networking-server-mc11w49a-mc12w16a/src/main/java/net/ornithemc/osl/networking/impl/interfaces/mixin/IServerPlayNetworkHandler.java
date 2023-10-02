package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

import net.minecraft.network.packet.CustomPayloadPacket;

public interface IServerPlayNetworkHandler {

	boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet);

	boolean osl$networking$isPlayReady();

	void osl$networking$registerClientChannels(Set<String> channels);

	boolean osl$networking$isRegisteredClientChannel(String channel);

}
