package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
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

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin implements CustomPayloadPacketAccess {

	@Conditional(minecraftVersion = @Version("<1.13-pre3"))
	@Shadow
	private String f_32230462;
	@Conditional(minecraftVersion = @Version(">=1.13-pre3"))
	@Shadow
	private Identifier channel;
	@Conditional(minecraftVersion = @Version("<14w31a"))
	@Shadow
	private byte[] f_15231323;
	@Conditional(minecraftVersion = @Version(">=14w31a"))
	@Shadow
	private PacketByteBuf data;

	@Conditional(minecraftVersion = @Version("<1.13-pre3"))
	@ModifyConstant(
		method = "read",
		constant = @Constant(
			intValue = 20
		)
	)
	private int osl$networking$modifyMaxChannelLength(int maxLength) {
		return StringChannelIdentifierParser.MAX_LENGTH;
	}

	@Override
	public NamespacedIdentifier osl$networking$getChannel() {
		if (Networking.CLIENTBOUND_STRING_CHANNELS) {
			return StringChannelIdentifierParser.fromString(this.f_32230462);
		} else {
			return IdentifierChannelIdentifierParser.fromIdentifier(this.channel);
		}
	}

	@Override
	public PacketBuffer osl$networking$getData() {
		if (Networking.CLIENTBOUND_BYTE_ARRAY_DATA) {
			return PacketBuffers.wrap(this.f_15231323);
		} else {
			return PacketBuffers.wrapped(this.data.copy());
		}
	}
}
