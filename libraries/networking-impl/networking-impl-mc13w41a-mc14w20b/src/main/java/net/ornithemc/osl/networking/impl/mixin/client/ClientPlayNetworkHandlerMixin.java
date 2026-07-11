package net.ornithemc.osl.networking.impl.mixin.client;

import java.net.SocketAddress;
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
import net.minecraft.network.Connection;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.AddressParser;
import net.ornithemc.osl.networking.impl.HandshakePayload;
import net.ornithemc.osl.networking.impl.access.ClientNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.text.api.TextComponents;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin implements ClientNetworkHandlerAccess {

	@Shadow @Final
	private Minecraft minecraft;
	@Shadow @Final
	private Connection connection;

	@Unique
	private ClientConnectionContext connectionContext;
	/**
	 * Channels that the server is listening to.
	 */
	@Unique
	private Set<NamespacedIdentifier> serverChannels;

	@Inject(
		method = "handleLogin",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$handleLogin(CallbackInfo ci) {
		if (minecraft.isIntegratedServerRunning()) {
			IntegratedServer server = minecraft.getServer();
			String worldName = server.getWorldName();

			connectionContext = new ClientConnectionContext(minecraft, worldName);
		} else {
			SocketAddress address = connection.getAddress();

			String serverAddress = AddressParser.getAddress(address);
			int serverPort = AddressParser.getPort(address);

			connectionContext = new ClientConnectionContext(minecraft, serverAddress, serverPort);
		}

		// send channel registration data as soon as login occurs
		ClientPlayNetworkingImpl.sendNoCheck(HandshakePayload.CHANNEL, HandshakePayload.client());

		ClientConnectionEvents.LOGIN.invoker().accept(connectionContext);
	}

	@Inject(
		method = "handleDisconnect",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleDisconnect(DisconnectS2CPacket packet, CallbackInfo ci) {
		connectionContext.offerDisconnectReason(TextComponents.literal(packet.getReason().getFormattedString()));
	}

	@Inject(
		method = "onDisconnect",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleDisconnect(Text reason, CallbackInfo ci) {
		connectionContext.offerDisconnectReason(TextComponents.literal(reason.getFormattedString()));
	}

	@Inject(
		method = "handleCustomPayload",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
		if (ClientPlayNetworkingImpl.handlePacket(minecraft, (ClientPlayNetworkHandler)(Object)this, packet)) {
			ci.cancel();
		}
	}

	@Override
	public ClientConnectionContext osl$networking$connectionContext() {
		return connectionContext;
	}

	@Override
	public boolean osl$networking$isPlayReady() {
		return serverChannels != null;
	}

	@Override
	public boolean osl$networking$isPlayReady(NamespacedIdentifier channel) {
		return serverChannels != null && serverChannels.contains(channel);
	}

	@Override
	public void osl$networking$registerChannels(Set<NamespacedIdentifier> channels) {
		serverChannels = new LinkedHashSet<>(channels);
	}
}
