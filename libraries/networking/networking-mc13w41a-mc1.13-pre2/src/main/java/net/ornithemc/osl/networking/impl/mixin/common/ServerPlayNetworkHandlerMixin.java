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

import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.minecraft.text.Text;

import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import net.ornithemc.osl.networking.impl.CommonChannels;
import net.ornithemc.osl.networking.impl.NetworkingImpl;
import net.ornithemc.osl.networking.impl.interfaces.mixin.IServerPlayNetworkHandler;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin implements IServerPlayNetworkHandler {

	@Shadow @Final private MinecraftServer server;
	@Shadow @Final private ServerPlayerEntity player;

	/**
	 * Channels that the client is listening to.
	 */
	@Unique private final Set<String> clientChannels = new LinkedHashSet<>();

	@Inject(
		method = "onDisconnect",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$disconnect(Text reason, CallbackInfo ci) {
		ServerConnectionEvents.DISCONNECT.invoker().accept(server, player);
		clientChannels.clear();
	}

	@Inject(
		method = "handleCustomPayload",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleCustomPayload(CustomPayloadC2SPacket packet, CallbackInfo ci) {
		if (ServerPlayNetworkingImpl.handle(server, (ServerPlayNetworkHandler)(Object)this, player, packet)) {
			ci.cancel();
		}
	}

	@Override
	public void osl$networking$registerClientChannels(Set<String> channels) {
		clientChannels.addAll(channels);

		ServerPlayNetworkingImpl.doSend(player, CommonChannels.CHANNELS, data -> {
			NetworkingImpl.writeChannels(data, ServerPlayNetworkingImpl.LISTENERS.keySet());
		});
	}

	@Override
	public boolean osl$networking$isRegisteredClientChannel(String channel) {
		return clientChannels.contains(channel);
	}
}
