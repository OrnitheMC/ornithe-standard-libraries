package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.IdentifierChannelParser;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin implements CustomPayloadPacketAccess {

	@Shadow private Identifier channel;
	@Shadow private PacketByteBuf data;

	@Override
	public Channel osl$networking$getChannel() {
		return IdentifierChannelParser.fromIdentifier(channel);
	}

	@Override
	public PacketByteBuf osl$networking$getData() {
		return PacketBuffers.copy(data);
	}
}
