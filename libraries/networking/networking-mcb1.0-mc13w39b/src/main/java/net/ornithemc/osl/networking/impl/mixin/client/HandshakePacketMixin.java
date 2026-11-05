package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.HandshakePacket;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.networking.impl.Constants;

@Conditional(minecraftVersion = @Version("<11w49a"))
@Mixin(HandshakePacket.class)
public class HandshakePacketMixin {

	@Shadow
	private String username;

	@Inject(
		method = "<init>(Ljava/lang/String;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$modifyHandshakeForOsl(CallbackInfo ci) {
		this.username = Constants.OSL_HANDSHAKE_KEY;
	}
}
