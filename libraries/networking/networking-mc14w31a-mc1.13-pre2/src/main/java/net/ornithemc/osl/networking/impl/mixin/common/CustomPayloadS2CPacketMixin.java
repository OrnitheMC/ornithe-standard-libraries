package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.api.StringChannelParser;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin implements CustomPayloadPacketAccess {

	@Shadow private String channel;
	@Shadow private PacketByteBuf data;

	@ModifyConstant(
		method = "read",
		constant = @Constant(
			intValue = 20
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
	public PacketByteBuf osl$networking$getData() {
		return PacketBuffers.copy(data);
	}
}
