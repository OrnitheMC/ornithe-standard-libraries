package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.resource.Identifier;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.IdentifierChannelIdentifierParser;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import net.ornithemc.osl.networking.impl.Networking;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;

@Mixin(CustomPayloadC2SPacket.class)
public class CustomPayloadC2SPacketMixin implements CustomPayloadPacketAccess {

	@Conditional(minecraftVersion = @Version("<1.13-pre4"))
	@Shadow
	private String f_08816511;
	@Conditional(minecraftVersion = @Version(">=1.13-pre4"))
	@Shadow
	private Identifier channel;
	@Conditional(minecraftVersion = @Version("<14w31a"))
	@Shadow
	private byte[] f_46904753;
	@Conditional(minecraftVersion = @Version(">=14w31a"))
	@Shadow
	private PacketByteBuf data;

	@Conditional(minecraftVersion = @Version("<1.13-pre4"))
	@ModifyConstant(
		method = "read",
		constant = @Constant(
			intValue = 20
		)
	)
	private int osl$networking$modifyMaxChannelLength(int maxLength) {
		return StringChannelIdentifierParser.MAX_LENGTH;
	}

	@Conditional(minecraftVersion = @Version(">=14w31a"))
	@Inject(
		method = "handle(Lnet/minecraft/server/network/handler/ServerPlayPacketHandler;)V",
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
		if (Networking.SERVERBOUND_STRING_CHANNELS) {
			return StringChannelIdentifierParser.fromString(this.f_08816511);
		} else {
			return IdentifierChannelIdentifierParser.fromIdentifier(this.channel);
		}
	}

	@Override
	public PacketBuffer osl$networking$getData() {
		if (Networking.SERVERBOUND_BYTE_ARRAY_DATA) {
			return PacketBuffers.wrap(this.f_46904753);
		} else {
			return PacketBuffers.wrapped(this.data.copy());
		}
	}
}
