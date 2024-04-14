package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;

public interface IServerPlayNetworkHandler {

	ServerPlayerEntity osl$networking$getPlayer();

	boolean osl$networking$isPlayReady();

	void osl$networking$registerClientChannels(Set<String> channels);

	boolean osl$networking$isRegisteredClientChannel(String channel);

}
