package net.ornithemc.osl.networking.impl.mixin.client;

import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientNetworkHandler;

import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.CustomPayloadPacket;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.interfaces.mixin.INetworkHandler;

@Mixin(ClientNetworkHandler.class)
public class ClientNetworkHandlerMixin implements INetworkHandler {

	@Shadow @Final private Minecraft minecraft;

	/**
	 * Channels that the server is listening to.
	 */
	@Unique private Set<String> serverChannels;

	@Inject(
		method = "handleLogin",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$handleLogin(CallbackInfo ci) {
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

	@Override
	public boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet) {
		return ClientPlayNetworkingImpl.handle(minecraft, (ClientNetworkHandler)(Object)this, packet);
	}

	@Override
	public boolean osl$networking$isPlayReady() {
		return serverChannels != null;
	}

	@Override
	public void osl$networking$registerChannels(Set<String> channels) {
		serverChannels = new LinkedHashSet<>(channels);
	}

	@Override
	public boolean osl$networking$isRegisteredChannel(String channel) {
		return serverChannels != null && serverChannels.contains(channel);
	}
}
