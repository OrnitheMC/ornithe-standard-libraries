package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

	@Shadow @Final private MinecraftServer server;

	@Inject(
		method = "onLogin",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$login(Connection connection, ServerPlayerEntity player, CallbackInfo ci) {
		ServerConnectionEvents.LOGIN.invoker().accept(server, player);
	}
}
