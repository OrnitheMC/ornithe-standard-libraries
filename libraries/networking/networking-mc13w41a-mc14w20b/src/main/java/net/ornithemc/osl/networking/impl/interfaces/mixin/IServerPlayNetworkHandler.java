package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.Set;

public interface IServerPlayNetworkHandler {

	void osl$networking$registerClientChannels(Set<String> channels);

	boolean osl$networking$isRegisteredClientChannel(String channel);

}
