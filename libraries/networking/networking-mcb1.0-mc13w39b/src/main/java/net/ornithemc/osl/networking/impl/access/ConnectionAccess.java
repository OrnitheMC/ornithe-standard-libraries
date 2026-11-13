package net.ornithemc.osl.networking.impl.access;

import java.net.SocketAddress;

public interface ConnectionAccess {

	default SocketAddress getAddress() {
		throw new AbstractMethodError();
	}
}
