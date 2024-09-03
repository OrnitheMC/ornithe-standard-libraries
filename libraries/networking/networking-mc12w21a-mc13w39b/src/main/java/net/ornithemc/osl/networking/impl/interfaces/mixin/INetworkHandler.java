package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

public interface INetworkHandler {

	boolean osl$networking$isPlayReady();

	void osl$networking$registerChannels(Set<String> channels);

	boolean osl$networking$isRegisteredChannel(String channel);

}
