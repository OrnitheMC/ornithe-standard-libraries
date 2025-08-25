package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.HandshakePacket;

import net.ornithemc.osl.networking.impl.Constants;

@Mixin(HandshakePacket.class)
public class HandshakePacketMixin {

	@Shadow private String key;

	@Inject(
		method = "<init>(Ljava/lang/String;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$modifyHandshakeForOsl(CallbackInfo ci) {
		key = Constants.OSL_HANDSHAKE_KEY;
	}
}
