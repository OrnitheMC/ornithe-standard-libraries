package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

@Conditional(minecraftVersion = @Version("<1.13-pre3"))
@Mixin(CustomPayloadS2CPacket.class)
public interface CustomPayloadS2CPacketAccess {

	@Conditional(minecraftVersion = @Version("<14w31a"))
	@Invoker("<init>")
	static CustomPayloadS2CPacket of(String channel, byte[] data) {
		throw new AbstractMethodError();
	}

	@Conditional(minecraftVersion = @Version(">=14w31a"))
	@Invoker("<init>")
	static CustomPayloadS2CPacket of(String channel, PacketByteBuf data) {
		throw new AbstractMethodError();
	}
}
