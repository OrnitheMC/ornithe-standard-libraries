package net.ornithemc.osl.networking.impl.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.networking.api.server.ServerConnectionEvents.DisconnectContext;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents.LoginContext;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents.PlayReadyContext;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.text.api.TextComponent;

public final class ServerConnectionContext implements LoginContext, PlayReadyContext, DisconnectContext {

	private final MinecraftServer server;
	private final ServerPlayNetworkHandler networkHandler;

	private TextComponent disconnectReason;

	public ServerConnectionContext(MinecraftServer server, ServerPlayNetworkHandler networkHandler) {
		this.server = server;
		this.networkHandler = networkHandler;
	}

	@Override
	public MinecraftServer server() {
		return this.server;
	}

	@Override
	public ServerPlayerEntity player() {
		return ((ServerNetworkHandlerAccess) this.networkHandler).osl$networkin$player();
	}

	@Override
	public TextComponent disconnectReason() {
		return this.disconnectReason;
	}

	public void offerDisconnectReason(TextComponent disconnectReason) {
		if (this.disconnectReason == null) {
			this.disconnectReason = disconnectReason;
		}
	}
}
