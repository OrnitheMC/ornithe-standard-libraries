package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.IdentifierChannelIdentifierParser;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketBuffers;
import net.ornithemc.osl.networking.impl.access.CustomPayloadPacketAccess;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin implements CustomPayloadPacketAccess {

	@Shadow private Identifier channel;
	@Shadow private PacketByteBuf data;

	@Override
	public NamespacedIdentifier osl$networking$getChannel() {
		return IdentifierChannelIdentifierParser.fromIdentifier(channel);
	}

	@Override
	public PacketBuffer osl$networking$getData() {
		return PacketBuffers.wrapped(data.copy());
	}
}
