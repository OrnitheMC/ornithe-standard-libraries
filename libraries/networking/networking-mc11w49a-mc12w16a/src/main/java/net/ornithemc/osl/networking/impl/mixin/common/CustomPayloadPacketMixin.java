package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.packet.CustomPayloadPacket;

import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.StringChannelParser;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;

@Mixin(CustomPayloadPacket.class)
public class CustomPayloadPacketMixin implements CustomPayloadPacketAccess {

	@Shadow private String channel;
	@Shadow private byte[] data;

	@ModifyConstant(
		method = "read",
		constant = @Constant(
			intValue = 16
		)
	)
	private int osl$networking$modifyMaxChannelLength(int maxLength) {
		return StringChannelParser.MAX_LENGTH;
	}

	@Override
	public Channel osl$networking$getChannel() {
		return StringChannelParser.fromString(channel);
	}

	@Override
	public byte[] osl$networking$getData() {
		return data;
	}
}
