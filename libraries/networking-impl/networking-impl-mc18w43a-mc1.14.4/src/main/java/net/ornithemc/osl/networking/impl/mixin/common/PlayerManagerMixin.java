package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

	@Shadow @Final private MinecraftServer server;

	@Inject(
		method = "onLogin",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$handleLogin(CallbackInfo ci, @Local ServerPlayerEntity player) {
		ServerNetworkHandlerAccess networkHandler = (ServerNetworkHandlerAccess) player.networkHandler;
		ServerConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

		ServerConnectionEvents.LOGIN.invoker().accept(connectionContext);
	}

	@Inject(
		method = "remove",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleDisconnect(CallbackInfo ci, @Local ServerPlayerEntity player) {
		ServerNetworkHandlerAccess networkHandler = (ServerNetworkHandlerAccess) player.networkHandler;
		ServerConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

		ServerConnectionEvents.DISCONNECT.invoker().accept(connectionContext);
	}
}
