package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

import net.minecraft.resource.Identifier;

public interface IClientPlayNetworkHandler {

	void osl$networking$registerServerChannels(Set<Identifier> channels);

	boolean osl$networking$isRegisteredServerChannel(Identifier channel);

}
