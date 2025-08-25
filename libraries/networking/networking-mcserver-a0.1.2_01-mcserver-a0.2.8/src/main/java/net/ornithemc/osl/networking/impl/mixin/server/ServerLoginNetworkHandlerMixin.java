package net.ornithemc.osl.networking.impl.mixin.server;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.network.packet.HandshakePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerLoginNetworkHandler;

import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.Constants;
import net.ornithemc.osl.networking.impl.HandshakePayload;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {

	@Shadow @Final private MinecraftServer server;

	/**
	 * is the client also running OSL?
	 */
	@Unique private boolean ornithe;

	@Inject(
		method = "handleHandshake",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleHandshake(HandshakePacket packet, CallbackInfo ci) {
		if (Constants.OSL_HANDSHAKE_KEY.equals(packet.key)) {
			ornithe = true;
		}
	}

	@Inject(
		method = "acceptLogin",
		locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$handleLogin(CallbackInfo ci, ServerPlayerEntity player) {
		if (player != null) {
			if (ornithe) {
				// send channel registration data as soon as login occurs
				ServerPlayNetworkingImpl.sendNoCheck(player, HandshakePayload.CHANNEL, HandshakePayload.server());
			}

			ServerConnectionEvents.LOGIN.invoker().accept(server, player);
		}
	}
}
