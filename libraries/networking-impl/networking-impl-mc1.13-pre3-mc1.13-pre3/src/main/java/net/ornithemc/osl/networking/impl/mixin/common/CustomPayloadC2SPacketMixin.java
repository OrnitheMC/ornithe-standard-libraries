package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;

@Mixin(CustomPayloadC2SPacket.class)
public class CustomPayloadC2SPacketMixin implements CustomPayloadPacketAccess {

	@Shadow private String channel;
	@Shadow private PacketByteBuf data;

	@ModifyConstant(
		method = "read",
		constant = @Constant(
			intValue = 20
		)
	)
	private int osl$networking$modifyMaxChannelLength(int maxLength) {
		return StringChannelIdentifierParser.MAX_LENGTH;
	}

	@Inject(
		method = "m_9429910",
		cancellable = true,
		at = @At(
			value = "INVOKE",
			shift = Shift.AFTER,
			target = "Lnet/minecraft/server/network/handler/ServerPlayPacketHandler;handleCustomPayload(Lnet/minecraft/network/packet/c2s/play/CustomPayloadC2SPacket;)V"
		)
	)
	private void osl$networking$skipBufferRelease(CallbackInfo ci) {
		// there's a call to ByteBuf.release() that we want to skip
		// so that we can queue packet handling to the main thread
		// Vanilla does this by throwing an exception but for the sake
		// of version compat we cannot use the same approach
		ci.cancel();
	}

	@Override
	public NamespacedIdentifier osl$networking$getChannel() {
		return StringChannelIdentifierParser.fromString(channel);
	}

	@Override
	public PacketBuffer osl$networking$getData() {
		return PacketBuffers.wrapped(data.copy());
	}
}
