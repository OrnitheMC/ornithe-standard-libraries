package net.ornithemc.osl.networking.impl;

import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.networking.api.Channel;

public interface CustomPayloadPacketFactory {

	Packet create(Channel channel, byte[] data);

}
