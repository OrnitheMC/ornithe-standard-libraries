package net.ornithemc.osl.networking.impl.mixin.client;

import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.CustomPayloadPacket;

import net.ornithemc.osl.networking.api.Channel;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.HandshakePayload;
import net.ornithemc.osl.networking.impl.access.NetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;

@Mixin(ClientNetworkHandler.class)
public class ClientNetworkHandlerMixin implements NetworkHandlerAccess {

	@Shadow private Minecraft minecraft;
	@Shadow private ClientWorld world;

	/**
	 * Channels that the server is listening to.
	 */
	@Unique private Set<Channel> serverChannels;

	@Inject(
		method = "handleLogin",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$handleLogin(CallbackInfo ci) {
		// send channel registration data as soon as login occurs
		ClientPlayNetworkingImpl.sendNoCheck(HandshakePayload.CHANNEL, HandshakePayload.client());

		ClientConnectionEvents.LOGIN.invoker().accept(minecraft);
	}

	@Inject(
		method = "onDisconnect",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleDisconnect(CallbackInfo ci) {
		ClientConnectionEvents.DISCONNECT.invoker().accept(minecraft);
		serverChannels = null;
	}

	@Inject(
		method = "handleCustomPayload",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleCustomPayload(CustomPayloadPacket packet, CallbackInfo ci) {
		if (ClientPlayNetworkingImpl.handle(minecraft, (ClientNetworkHandler)(Object)this, packet)) {
			ci.cancel();
		}
	}

	@Override
	public boolean osl$networking$canRunOffMainThread() {
		return minecraft != null && minecraft.world != null && minecraft.player != null && world != null;
	}

	@Override
	public boolean osl$networking$isPlayReady() {
		return serverChannels != null;
	}

	@Override
	public boolean osl$networking$isPlayReady(Channel channel) {
		return serverChannels != null && serverChannels.contains(channel);
	}

	@Override
	public void osl$networking$registerChannels(Set<Channel> channels) {
		serverChannels = new LinkedHashSet<>(channels);
	}
}
