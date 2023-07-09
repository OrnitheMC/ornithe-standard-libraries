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
import net.minecraft.network.packet.CustomPayloadPacket;

import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.CommonChannels;
import net.ornithemc.osl.networking.impl.NetworkingInitializer;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientNetworkHandler;

@Mixin(ClientNetworkHandler.class)
public class ClientNetworkHandlerMixin implements IClientNetworkHandler {

	@Shadow @Final private Minecraft minecraft;

	/**
	 * Channels that the server is listening to.
	 */
	@Unique private final Set<String> serverChannels = new LinkedHashSet<>();

	@Inject(
		method = "handleLogin",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$handleLogin(CallbackInfo ci) {
		ClientPlayNetworkingImpl.doSend(CommonChannels.CHANNELS, data -> {
			NetworkingInitializer.writeChannels(data, ClientPlayNetworkingImpl.LISTENERS.keySet());
		});

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
		serverChannels.clear();
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
	public void osl$networking$registerServerChannels(Set<String> channels) {
		serverChannels.addAll(channels);
	}

	@Override
	public boolean osl$networking$isRegisteredServerChannel(String channel) {
		return serverChannels.contains(channel);
	}
}
