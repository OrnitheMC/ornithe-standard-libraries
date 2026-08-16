package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.RemoteConnection;
import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.networking.impl.Connections;

@Mixin(RemoteConnection.class)
public class RemoteConnectionMixinNew {

	@Shadow private PacketHandler listener;

	@Inject(
		method = "read",
		cancellable = true,
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z"
		)
	)
	private void osl$networking$asyncCustomPayloads(CallbackInfoReturnable<Boolean> cir, @Local Packet packet) {
		if (Connections.handleAsyncPacket(packet, listener)) {
			cir.setReturnValue(true);
		}
	}
}
