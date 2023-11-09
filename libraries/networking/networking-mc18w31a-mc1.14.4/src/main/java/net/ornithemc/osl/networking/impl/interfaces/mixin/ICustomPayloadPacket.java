package net.ornithemc.osl.networking.impl.interfaces.mixin;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Identifier;

public interface ICustomPayloadPacket {

	Identifier osl$networking$getChannel();

	PacketByteBuf osl$networking$getData();

}
