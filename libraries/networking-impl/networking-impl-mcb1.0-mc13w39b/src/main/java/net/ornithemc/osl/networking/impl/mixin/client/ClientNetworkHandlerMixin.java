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
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.RemoteConnection;
import net.minecraft.network.packet.CustomPayloadPacket;
import net.minecraft.network.packet.DisconnectPacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.integrated.IntegratedServer;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.AddressParser;
import net.ornithemc.osl.networking.impl.HandshakePayload;
import net.ornithemc.osl.networking.impl.Networking;
import net.ornithemc.osl.networking.impl.access.ClientNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.MinecraftAccess;
import net.ornithemc.osl.networking.impl.access.PacketHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.text.api.TextComponents;

@Mixin(ClientNetworkHandler.class)
public class ClientNetworkHandlerMixin implements ClientNetworkHandlerAccess, PacketHandlerAccess {

	@Shadow @Final
	private Minecraft minecraft;
	@Shadow @Final
	private ClientWorld world;
	@Conditional(minecraftVersion = @Version("<12w21a"))
	@Shadow @Final
	private RemoteConnection f_62887500;

	@Unique
	private ClientConnectionContext connectionContext;
	/**
	 * Channels that the server is listening to.
	 */
	@Unique
	private Set<NamespacedIdentifier> serverChannels;

	@Inject(
		method = "<init>*",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$initConnectionContext(CallbackInfo ci) {
		if (Networking.INTEGRATED_SERVER_EXISTS && this.minecraft.isIntegratedServerRunning()) {
			IntegratedServer server = this.minecraft.getServer();
			String worldName = server.getWorldName();

			this.connectionContext = new ClientConnectionContext(this.minecraft, worldName);
		} else {
			SocketAddress address = this.f_62887500.getAddress();

			String serverAddress = AddressParser.getAddress(address);
			int serverPort = AddressParser.getPort(address);

			this.connectionContext = new ClientConnectionContext(this.minecraft, serverAddress, serverPort);
		}
	}

	@Inject(
		method = "handleLogin",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$networking$handleLogin(CallbackInfo ci) {
		if (Networking.CUSTOM_PAYLOAD_EXISTS) {
			// send channel registration data as soon as login occurs
			ClientPlayNetworkingImpl.sendNoCheck(HandshakePayload.CHANNEL, HandshakePayload.client());
		}

		ClientConnectionEvents.LOGIN.invoker().accept(this.connectionContext);
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

	@Conditional(minecraftVersion = @Version(">=12w18a"))
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
	public ClientConnectionContext osl$networking$connectionContext() {
		return this.connectionContext;
	}

	@Override
	public boolean osl$networking$canRunOffMainThread() {
		return this.minecraft != null && ((MinecraftAccess) this.minecraft).osl$networking$hasWorldAndPlayer() && this.world != null;
	}

	@Override
	public boolean osl$networking$isPlayReady() {
		return this.serverChannels != null;
	}

	@Override
	public boolean osl$networking$isPlayReady(NamespacedIdentifier channel) {
		return this.serverChannels != null && this.serverChannels.contains(channel);
	}

	@Override
	public void osl$networking$registerChannels(Set<NamespacedIdentifier> channels) {
		this.serverChannels = new LinkedHashSet<>(channels);
	}

	@Override
	public boolean osl$networking$handleCustomPayload(Packet packet) {
		return ClientPlayNetworkingImpl.handlePacket(this.minecraft, (ClientNetworkHandler)(Object)this, packet);
	}
}
