package net.ornithemc.osl.networking.impl.mixin.common;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	private static final boolean INTEGRATED_SERVER_REMOVES_PLAYERS_ON_STOP = MinecraftVersion.resolve().compareTo("14w06a") >= 0;

	@Shadow
	private PlayerManager playerManager;

	@Shadow
	private boolean isDedicated() { return false; }

	@Inject(
		method = "stop",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$stop(CallbackInfo ci) {
		if (!INTEGRATED_SERVER_REMOVES_PLAYERS_ON_STOP || this.isDedicated()) {
			@SuppressWarnings("unchecked")
			List<ServerPlayerEntity> players = (List<ServerPlayerEntity>) this.playerManager.players;

			for (ServerPlayerEntity player : players) {
				ServerNetworkHandlerAccess networkHandler = (ServerNetworkHandlerAccess) player.networkHandler;
				ServerConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

				ServerConnectionEvents.DISCONNECT.invoker().accept(connectionContext);
			}
		}
	}
}
