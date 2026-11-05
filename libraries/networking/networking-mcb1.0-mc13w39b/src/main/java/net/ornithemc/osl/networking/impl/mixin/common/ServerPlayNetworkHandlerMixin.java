package net.ornithemc.osl.networking.impl.mixin.common;

import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.CustomPayloadPacket;
import net.minecraft.network.packet.DisconnectPacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.GameSide;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.impl.access.PacketHandlerAccess;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;
import net.ornithemc.osl.text.api.TextComponents;

@Conditional(minecraftVersion = @Version(value = ">=12w18a", gameSide = GameSide.CLIENT))
@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin implements ServerNetworkHandlerAccess, PacketHandlerAccess {

	@Shadow @Final
	private MinecraftServer server;

	@Shadow
	private ServerPlayerEntity player;

	@Unique
	private ServerConnectionContext connectionContext;
	/**
	 * Channels that the client is listening to.
	 */
	@Unique
	private Set<NamespacedIdentifier> clientChannels;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$initConnectionContext(CallbackInfo ci) {
		this.connectionContext = new ServerConnectionContext(this.server, (ServerPlayNetworkHandler) (Object) this);
	}

	@Inject(
		method = "handleDisconnect",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleDisconnect(DisconnectPacket packet, CallbackInfo ci) {
		this.connectionContext.offerDisconnectReason(TextComponents.resolve(packet.reason));
	}

	@Inject(
		method = "onDisconnect",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleDisconnect(String reason, Object[] args, CallbackInfo ci) {
		this.connectionContext.offerDisconnectReason(args == null
			? TextComponents.translatable(reason)
			: TextComponents.translatable(reason, args));
	}

	@Conditional(minecraftVersion = @Version(">=12w17a"))
	@Inject(
		method = "handleCustomPayload",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleCustomPayload(CustomPayloadPacket packet, CallbackInfo ci) {
		if (this.osl$networking$handleCustomPayload(packet)) {
			ci.cancel();
		}
	}

	@Override
	public ServerConnectionContext osl$networking$connectionContext() {
		return this.connectionContext;
	}

	@Override
	public boolean osl$networking$canRunOffMainThread() {
		return true;
	}

	@Override
	public boolean osl$networking$isPlayReady() {
		return this.clientChannels != null;
	}

	@Override
	public boolean osl$networking$isPlayReady(NamespacedIdentifier channel) {
		return this.clientChannels != null && this.clientChannels.contains(channel);
	}

	@Override
	public void osl$networking$registerChannels(Set<NamespacedIdentifier> channels) {
		this.clientChannels = new LinkedHashSet<>(channels);
	}

	@Override
	public boolean osl$networking$handleCustomPayload(Packet packet) {
		return ServerPlayNetworkingImpl.handlePacket(this.server, (ServerPlayNetworkHandler)(Object)this, this.player, packet);
	}
}
