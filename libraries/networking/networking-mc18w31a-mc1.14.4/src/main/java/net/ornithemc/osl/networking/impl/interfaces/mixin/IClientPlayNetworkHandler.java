package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

import net.minecraft.resource.Identifier;

public interface IClientPlayNetworkHandler {

	boolean osl$networking$isPlayReady();

	void osl$networking$registerServerChannels(Set<Identifier> channels);

	boolean osl$networking$isRegisteredServerChannel(Identifier channel);

}
