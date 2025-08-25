package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;

@Mixin(Packet.class)
public class PacketMixin {

	@Inject(
		method = "canBeHandledOffMainThread",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$asyncCustomPayloads(CallbackInfoReturnable<Boolean> cir) {
		// TODO: somehow only do this for channels OSL has listeners for
		if (this instanceof CustomPayloadPacketAccess) {
			cir.setReturnValue(true);
		}
	}
}
