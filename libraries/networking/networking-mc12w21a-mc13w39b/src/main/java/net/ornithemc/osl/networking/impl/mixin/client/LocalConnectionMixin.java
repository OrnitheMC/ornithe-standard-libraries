package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.LocalConnection;
import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.networking.impl.Connections;

@Mixin(LocalConnection.class)
public class LocalConnectionMixin {

	@Shadow private PacketHandler listener;

	@Inject(
		method = "accept",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$handlePacketsAsync(Packet packet, CallbackInfo ci) {
		if (Connections.checkAsyncHandling(packet, listener)) {
			ci.cancel();
		}
	}
}
