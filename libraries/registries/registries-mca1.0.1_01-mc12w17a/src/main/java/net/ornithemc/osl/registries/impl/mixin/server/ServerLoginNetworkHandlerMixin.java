package net.ornithemc.osl.registries.impl.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerLoginNetworkHandler;

import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import net.ornithemc.osl.registries.impl.Constants;
import net.ornithemc.osl.registries.impl.registry.sync.SyncedRegistriesPacketSerializer;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {

	@Inject(
		method = "acceptLogin",
		at = @At(
			value = "NEW",
			target = "net/minecraft/network/packet/LoginPacket"
		)
	)
	private void osl$idsync$syncRegistries(CallbackInfo ci, @Local ServerPlayerEntity player) {
		ServerPlayNetworking.sendNoCheck(player, Constants.OSL_REGISTRY_SYNC_CHANNEL, SyncedRegistriesPacketSerializer::serialize);
	}
}
