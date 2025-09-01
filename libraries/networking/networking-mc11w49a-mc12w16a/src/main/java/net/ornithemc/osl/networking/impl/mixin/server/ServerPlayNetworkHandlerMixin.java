package net.ornithemc.osl.networking.impl.mixin.server;

import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.CustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.interfaces.mixin.INetworkHandler;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin implements NetworkHandlerAccess, INetworkHandler {

	@Shadow private MinecraftServer server;
	@Shadow private ServerPlayerEntity player;

	/**
	 * Channels that the client is listening to.
	 */
	@Unique private Set<Channel> clientChannels;

	@Inject(
		method = "onDisconnect",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleDisconnect(CallbackInfo ci) {
		ServerConnectionEvents.DISCONNECT.invoker().accept(server, player);
		clientChannels = null;
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
	public boolean osl$networking$isPlayReady(Channel channel) {
		return clientChannels != null && clientChannels.contains(channel);
	}

	@Override
	public void osl$networking$registerChannels(Set<Channel> channels) {
		clientChannels = new LinkedHashSet<>(channels);
	}

	@Override
	public boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet) {
		return ServerPlayNetworkingImpl.handle(server, (ServerPlayNetworkHandler)(Object)this, player, packet);
	}
}
