package net.ornithemc.osl.networking.impl.access;

import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;

public interface ClientNetworkHandlerAccess extends NetworkHandlerAccess {

	ClientConnectionContext osl$networking$connectionContext();

}
