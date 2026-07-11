package net.ornithemc.osl.networking.impl.mixin.server;

import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.impl.CustomPayloadPacket;
import net.ornithemc.osl.networking.impl.access.PacketHandlerAccess;
import net.ornithemc.osl.networking.impl.access.ServerNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;
import net.ornithemc.osl.text.api.TextComponents;

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
		connectionContext = new ServerConnectionContext(server, (ServerPlayNetworkHandler) (Object) this);
	}

	@Inject(
		method = "onDisconnect",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleDisconnect(String reason, CallbackInfo ci) {
		connectionContext.offerDisconnectReason(TextComponents.literal(reason));
	}

	@Override
	public ServerConnectionContext osl$networking$connectionContext() {
		return connectionContext;
	}

	@Override
	public boolean osl$networking$canRunOffMainThread() {
		return true;
	}

	@Override
	public boolean osl$networking$isPlayReady() {
		return clientChannels != null;
	}

	@Override
	public boolean osl$networking$isPlayReady(NamespacedIdentifier channel) {
		return clientChannels != null && clientChannels.contains(channel);
	}

	@Override
	public void osl$networking$registerChannels(Set<NamespacedIdentifier> channels) {
		clientChannels = new LinkedHashSet<>(channels);
	}

	@Override
	public boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet) {
		return ServerPlayNetworkingImpl.handlePacket(server, (ServerPlayNetworkHandler)(Object)this, player, packet);
	}
}
