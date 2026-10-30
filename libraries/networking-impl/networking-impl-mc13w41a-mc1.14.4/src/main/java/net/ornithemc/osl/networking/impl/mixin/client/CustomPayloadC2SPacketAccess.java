package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

@Conditional(minecraftVersion = @Version("<1.13-pre4"))
@Mixin(CustomPayloadC2SPacket.class)
public interface CustomPayloadC2SPacketAccess {

	@Conditional(minecraftVersion = @Version("<14w31a"))
	@Invoker("<init>")
	static CustomPayloadC2SPacket of(String channel, byte[] data) {
		throw new AbstractMethodError();
	}

	@Conditional(minecraftVersion = @Version(">=14w31a"))
	@Invoker("<init>")
	static CustomPayloadC2SPacket of(String channel, PacketByteBuf data) {
		throw new AbstractMethodError();
	}
}
