package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

import net.minecraft.resource.Identifier;

public interface INetworkHandler {

	boolean osl$networking$isPlayReady();

	void osl$networking$registerChannels(Set<Identifier> channels);

	boolean osl$networking$isRegisteredChannel(Identifier channel);

}
