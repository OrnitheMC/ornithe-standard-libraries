package net.ornithemc.osl.networking.impl.mixin.server;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.PlayerManagerAccess;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Shadow
	private PlayerManager playerManager;

	@Inject(
		method = "stop",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$stop(CallbackInfo ci) {
		List<ServerPlayerEntity> players = ((PlayerManagerAccess) this.playerManager).osl$networking$getAll();

		for (ServerPlayerEntity player : players) {
			ServerNetworkHandlerAccess networkHandler = (ServerNetworkHandlerAccess) player.networkHandler;
			ServerConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

			ServerConnectionEvents.DISCONNECT.invoker().accept(connectionContext);
		}
	}
}
