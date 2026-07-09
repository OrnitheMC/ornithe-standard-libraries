package net.ornithemc.osl.networking.impl;

import java.net.SocketAddress;

public final class AddressParser {

	public static final int DEFAULT_PORT = 25565;

	public static String getAddress(SocketAddress socketAddress) {
		String address = socketAddress.toString();

		if (address.contains("/")) {
			address = address.substring(address.indexOf('/') + 1);
		}
		if (address.contains(":")) {
			address = address.substring(0, address.indexOf(':'));
		}

		return address;
	}

	public static int getPort(SocketAddress socketAddress) {
		String address = socketAddress.toString();

		if (address.contains(":")) {
			String port = address.substring(address.indexOf(':') + 1);

			try {
				return Integer.parseInt(port);
			} catch (NumberFormatException ignored) {
			}
		}

		return -1;
	}
}
