package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.client.world.ClientWorld;

import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.access.ClientNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	private ClientWorld world;

	@Shadow
	private ClientNetworkHandler getNetworkHandler() { return null; }

	@Inject(
		method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$disconnect(ClientWorld world, String title, CallbackInfo ci) {
		if (this.world != null && world == null) {
			ClientNetworkHandlerAccess networkHandler = (ClientNetworkHandlerAccess) getNetworkHandler();
			ClientConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

			ClientConnectionEvents.DISCONNECT.invoker().accept(connectionContext);
		}
	}
}
