package net.ornithemc.osl.networking.impl.access;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.networking.api.Channel;

public interface CustomPayloadPacketAccess {

	Channel osl$networking$getChannel();

	PacketByteBuf osl$networking$getData();

}
