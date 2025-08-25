package net.ornithemc.osl.networking.impl.access;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface NetworkHandlerAccess {

	boolean osl$networking$canRunOffMainThread();

	boolean osl$networking$isPlayReady();

	boolean osl$networking$isPlayReady(NamespacedIdentifier channel);

	void osl$networking$registerChannels(Set<NamespacedIdentifier> channels);

}
