package net.ornithemc.osl.networking.impl;

public final class ChannelSettings {

	private final boolean clientbound;
	private final boolean serverbound;

	public ChannelSettings() {
		this(true, true);
	}

	public ChannelSettings(boolean clientbound, boolean serverbound) {
		this.clientbound = clientbound;
		this.serverbound = serverbound;
	}

	public boolean is(ChannelSettings o) {
		if (this == o) {
			return true;
		}
		return clientbound == o.clientbound && serverbound == o.serverbound;
	}

	public boolean isClientbound() {
		return clientbound;
	}

	public boolean isServerbound() {
		return serverbound;
	}
}
