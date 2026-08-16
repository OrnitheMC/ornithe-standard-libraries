package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerLoginNetworkHandler;

import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {

	@Shadow @Final private MinecraftServer server;

	@Inject(
		method = "acceptLogin",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$handleLogin(CallbackInfo ci, @Local ServerPlayerEntity player) {
		if (player != null) {
			ServerNetworkHandlerAccess networkHandler = (ServerNetworkHandlerAccess) player.networkHandler;
			ServerConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

			ServerConnectionEvents.LOGIN.invoker().accept(connectionContext);
		}
	}
}
