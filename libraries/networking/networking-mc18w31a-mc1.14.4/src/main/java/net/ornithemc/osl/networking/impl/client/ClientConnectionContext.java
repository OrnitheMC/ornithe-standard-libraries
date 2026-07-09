package net.ornithemc.osl.networking.impl.client;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.networking.api.client.ClientConnectionEvents.DisconnectContext;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents.LoginContext;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents.PlayReadyContext;
import net.ornithemc.osl.text.api.TextComponent;

public final class ClientConnectionContext implements LoginContext, PlayReadyContext, DisconnectContext {

	private final Minecraft minecraft;
	private final String worldName;
	private final String serverAddress;
	private final int serverPort;

	private TextComponent disconnectReason;

	public ClientConnectionContext(Minecraft minecraft, String worldName) {
		this(minecraft, worldName, null, -1);
	}

	public ClientConnectionContext(Minecraft minecraft, String serverAddress, int serverPort) {
		this(minecraft, null, serverAddress, serverPort);
	}

	private ClientConnectionContext(Minecraft minecraft, String worldName, String serverAddress, int serverPort) {
		this.minecraft = minecraft;
		this.worldName = worldName;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	@Override
	public Minecraft minecraft() {
		return this.minecraft;
	}

	@Override
	public String worldName() {
		return this.worldName;
	}

	@Override
	public String serverAddress() {
		return this.serverAddress;
	}

	@Override
	public int serverPort() {
		return this.serverPort;
	}

	@Override
	public TextComponent disconnectReason() {
		return this.disconnectReason;
	}

	@Override
	public boolean isServerLocal() {
		return this.worldName != null;
	}

	public void setDisconnectReason(TextComponent disconnectReason) {
		this.disconnectReason = disconnectReason;
	}
}
