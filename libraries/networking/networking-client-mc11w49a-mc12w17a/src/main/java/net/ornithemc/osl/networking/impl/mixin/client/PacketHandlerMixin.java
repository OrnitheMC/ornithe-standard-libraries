package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.CustomPayloadPacket;

import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientNetworkHandler;

@Mixin(PacketHandler.class)
public class PacketHandlerMixin {

	@Inject(
		method = "handleCustomPayload",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleCustomPayload(CustomPayloadPacket packet, CallbackInfo ci) {
		if (this instanceof IClientNetworkHandler && ((IClientNetworkHandler)this).osl$networking$handleCustomPayload(packet)) {
			ci.cancel();
		}
	}
}
