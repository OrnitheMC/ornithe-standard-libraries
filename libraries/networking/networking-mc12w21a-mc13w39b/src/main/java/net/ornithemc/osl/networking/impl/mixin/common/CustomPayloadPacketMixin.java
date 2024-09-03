package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.packet.CustomPayloadPacket;

import net.ornithemc.osl.networking.api.Channels;

@Mixin(CustomPayloadPacket.class)
public class CustomPayloadPacketMixin {

	@ModifyConstant(
		method = "read",
		constant = @Constant(
			intValue = 20
		)
	)
	private int osl$networking$modifyMaxChannelLength(int maxLength) {
		return Channels.MAX_LENGTH;
	}
}
