package net.ornithemc.osl.networking.impl;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.networking.api.Channel;

@FunctionalInterface
public interface CustomPayloadPacketFactory {

	Packet<?> create(Channel channel, PacketByteBuf data);

}
