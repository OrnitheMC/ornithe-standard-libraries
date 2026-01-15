package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;

import net.minecraft.network.Connection;
import net.minecraft.network.handler.PacketHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.networking.impl.Connections;

@Mixin(Connection.class)
public class ConnectionMixin {

	@Shadow
	private boolean isClient;
	@Shadow
	private PacketHandler listener;

	@Inject(
		method = "channelRead0",
		cancellable = true,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/packet/Packet;canBeHandledOffMainThread()Z"
		)
	)
	private void osl$networking$asyncCustomPayloads(ChannelHandlerContext ctx, Packet packet, CallbackInfo ci) {
		if (Connections.handleAsyncPacket(packet, listener)) {
			ci.cancel();
		}
	}
}
