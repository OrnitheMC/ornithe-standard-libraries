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
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.LoginS2CPacket;
import net.minecraft.text.Text;

import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.CommonChannels;
import net.ornithemc.osl.networking.impl.NetworkingInitializer;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IClientPlayNetworkHandler;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin implements IClientPlayNetworkHandler {

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
	private void osl$networking$handleCustomPayload(LoginS2CPacket packet, CallbackInfo ci) {
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
	private void osl$networking$handleCustomPayload(Text reason, CallbackInfo ci) {
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
	private void osl$networking$handleCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
		if (ClientPlayNetworkingImpl.handle(minecraft, (ClientPlayNetworkHandler)(Object)this, packet)) {
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
