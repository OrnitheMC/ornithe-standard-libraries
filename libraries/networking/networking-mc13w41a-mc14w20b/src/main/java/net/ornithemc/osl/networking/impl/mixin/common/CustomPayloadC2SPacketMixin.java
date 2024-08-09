package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

import net.ornithemc.osl.networking.impl.interfaces.mixin.ICustomPayloadPacket;

@Mixin(CustomPayloadC2SPacket.class)
public class CustomPayloadC2SPacketMixin implements ICustomPayloadPacket {

	@Shadow private String channel;
	@Shadow private byte[] data;

	@Override
	public String osl$networking$getChannel() {
		return channel;
	}
	@Override
	public byte[] osl$networking$getData() {
		return data;
	}
}
