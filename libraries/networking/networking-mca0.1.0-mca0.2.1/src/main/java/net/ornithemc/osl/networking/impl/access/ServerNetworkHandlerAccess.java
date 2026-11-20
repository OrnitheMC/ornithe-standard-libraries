package net.ornithemc.osl.networking.impl.access;

import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;

public interface ServerNetworkHandlerAccess extends NetworkHandlerAccess {

	ServerConnectionContext osl$networking$connectionContext();

}
