package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.packet.CustomPayloadPacket;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;

@Conditional(minecraftVersion = @Version(">=11w49a"))
@Mixin(CustomPayloadPacket.class)
public class CustomPayloadPacketMixin implements CustomPayloadPacketAccess {

	@Shadow
	private String channel;
	@Shadow
	private byte[] data;

	@Conditional(minecraftVersion = @Version("<12w18a"))
	@ModifyConstant(
		method = "read",
		constant = @Constant(
			intValue = 16
		)
	)
	private int osl$networking$modifyMaxChannelLength1(int maxLength) {
		return StringChannelIdentifierParser.MAX_LENGTH;
	}

	@Conditional(minecraftVersion = @Version(">=12w18a"))
	@ModifyConstant(
		method = "read",
		constant = @Constant(
			intValue = 20
		)
	)
	private int osl$networking$modifyMaxChannelLength2(int maxLength) {
		return StringChannelIdentifierParser.MAX_LENGTH;
	}

	@Override
	public NamespacedIdentifier osl$networking$getChannel() {
		return StringChannelIdentifierParser.fromString(this.channel);
	}

	@Override
	public byte[] osl$networking$getData() {
		return this.data;
	}
}
