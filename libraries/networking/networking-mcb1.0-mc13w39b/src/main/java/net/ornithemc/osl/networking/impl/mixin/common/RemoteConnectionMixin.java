package net.ornithemc.osl.networking.impl.mixin.common;

import java.net.Socket;
import java.net.SocketAddress;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.RemoteConnection;
import net.minecraft.network.packet.Packet;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.networking.impl.Connections;
import net.ornithemc.osl.networking.impl.access.ConnectionAccess;

@Mixin(RemoteConnection.class)
public class RemoteConnectionMixin implements ConnectionAccess {

	@Conditional(minecraftVersion = @Version("<12w18a"))
	@Shadow
	private Socket f_47731358;
	@Shadow
	private PacketHandler listener;

	@Conditional(minecraftVersion = @Version("<b1.5"))
	@Inject(
		method = "read",
		cancellable = true,
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
		)
	)
	private void osl$networking$asyncCustomPayloads (CallbackInfo ci, @Local Packet packet) {
		if (Connections.handleAsyncPacket(packet, this.listener)) {
			ci.cancel();
		}
	}

	@Conditional(minecraftVersion = @Version(">=b1.5 <=1.6.1"))
	@Inject(
		method = "read",
		cancellable = true,
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
		)
	)
	private void osl$networking$asyncCustomPayloads2(CallbackInfoReturnable<Boolean> cir, @Local Packet packet) {
		if (Connections.handleAsyncPacket(packet, this.listener)) {
			cir.setReturnValue(true);
		}
	}

	@Conditional(minecraftVersion = @Version(">1.6.1"))
	@Inject(
		method = "read",
		cancellable = true,
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z"
		)
	)
	private void osl$networking$asyncCustomPayloads3(CallbackInfoReturnable<Boolean> cir, @Local Packet packet) {
		if (Connections.handleAsyncPacket(packet, this.listener)) {
			cir.setReturnValue(true);
		}
	}

	@Conditional(minecraftVersion = @Version("<12w18a"))
	@Override
	public SocketAddress getAddress() {
		return this.f_47731358.getRemoteSocketAddress();
	}
}
