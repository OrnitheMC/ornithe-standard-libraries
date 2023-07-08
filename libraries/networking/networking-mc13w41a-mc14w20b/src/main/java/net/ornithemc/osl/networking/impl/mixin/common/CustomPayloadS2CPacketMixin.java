package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

import net.ornithemc.osl.networking.impl.interfaces.mixin.ICustomPayloadPacket;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin implements ICustomPayloadPacket {

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
