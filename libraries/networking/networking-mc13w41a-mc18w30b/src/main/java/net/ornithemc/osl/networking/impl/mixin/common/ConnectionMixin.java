package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;

import net.minecraft.client.network.handler.ClientPlayPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.handler.PacketHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.handler.ServerPlayPacketHandler;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.networking.impl.Connections;

@Conditional(minecraftVersion = @Version("<14w21a"))
@Mixin(Connection.class)
public class ConnectionMixin {

	@Shadow
	private PacketHandler listener;

	@Inject(
		method = "channelRead0",
		cancellable = true,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/packet/Packet;m_21917298()Z"
		)
	)
	private void osl$networking$asyncCustomPayloads(ChannelHandlerContext ctx, Packet packet, CallbackInfo ci) {
		if (packet instanceof CustomPayloadS2CPacket && this.listener instanceof ClientPlayPacketHandler) {
			if (Connections.handleAsyncPacket((CustomPayloadS2CPacket) packet, (ClientPlayPacketHandler) this.listener, ClientPlayPacketHandler::handleCustomPayload)) {
				ci.cancel();
			}
		}
		if (packet instanceof CustomPayloadC2SPacket && this.listener instanceof ServerPlayPacketHandler) {
			if (Connections.handleAsyncPacket((CustomPayloadC2SPacket) packet, (ServerPlayPacketHandler) this.listener, ServerPlayPacketHandler::handleCustomPayload)) {
				ci.cancel();
			}
		}
	}
}
