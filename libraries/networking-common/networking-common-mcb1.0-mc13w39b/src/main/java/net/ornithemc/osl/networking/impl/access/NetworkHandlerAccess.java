package net.ornithemc.osl.networking.impl.access;

import java.util.Set;

import net.ornithemc.osl.networking.api.Channel;

public interface NetworkHandlerAccess {

	boolean osl$networking$canRunOffMainThread();

	boolean osl$networking$isPlayReady();

	boolean osl$networking$isPlayReady(Channel channel);

	void osl$networking$registerChannels(Set<Channel> channels);

}
