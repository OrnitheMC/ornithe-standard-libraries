package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

public interface IClientPlayNetworkHandler {

	void osl$networking$registerServerChannels(Set<String> channels);

	boolean osl$networking$isRegisteredServerChannel(String channel);

}
