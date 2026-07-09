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
import net.minecraft.client.world.MultiplayerWorld;
import net.minecraft.network.Connection;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.AddressParser;
import net.ornithemc.osl.networking.impl.CustomPayloadPacket;
import net.ornithemc.osl.networking.impl.access.ClientNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.PacketHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.text.api.TextComponents;

@Mixin(ClientNetworkHandler.class)
public class ClientNetworkHandlerMixin implements ClientNetworkHandlerAccess, PacketHandlerAccess {

	@Shadow @Final
	private Minecraft minecraft;
	@Shadow @Final
	private MultiplayerWorld world;
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
		{
			SocketAddress address = ((ConnectionAccessor) connection).osl$networking$accessAddress();

			String serverAddress = AddressParser.getAddress(address);
			int serverPort = AddressParser.getPort(address);

			connectionContext = new ClientConnectionContext(minecraft, serverAddress, serverPort);
		}

		ClientConnectionEvents.LOGIN.invoker().accept(connectionContext);
	}

	@Inject(
		method = "onDisconnect",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handleDisconnect(String reason, Object[] args, CallbackInfo ci) {
		connectionContext.setDisconnectReason(args == null
			? TextComponents.translatable(reason)
			: TextComponents.translatable(reason, args));
	}

	@Override
	public ClientConnectionContext osl$networking$connectionContext() {
		return connectionContext;
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
	public boolean osl$networking$isPlayReady(NamespacedIdentifier channel) {
		return serverChannels != null && serverChannels.contains(channel);
	}

	@Override
	public void osl$networking$registerChannels(Set<NamespacedIdentifier> channels) {
		serverChannels = new LinkedHashSet<>(channels);
	}

	@Override
	public boolean osl$networking$handleCustomPayload(CustomPayloadPacket packet) {
		return ClientPlayNetworkingImpl.handlePacket(minecraft, (ClientNetworkHandler)(Object)this, packet);
	}
}
