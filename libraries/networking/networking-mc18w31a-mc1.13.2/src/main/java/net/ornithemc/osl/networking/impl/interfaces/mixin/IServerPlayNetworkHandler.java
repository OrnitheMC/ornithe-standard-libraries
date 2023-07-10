package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

import net.minecraft.resource.Identifier;

public interface IServerPlayNetworkHandler {

	void osl$networking$registerClientChannels(Set<Identifier> channels);

	boolean osl$networking$isRegisteredClientChannel(Identifier channel);

}
