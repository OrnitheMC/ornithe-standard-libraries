package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import net.ornithemc.osl.registries.impl.Constants;
import net.ornithemc.osl.registries.impl.registry.sync.SyncedRegistriesPacketSerializer;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

	@Shadow @Final
	private MinecraftServer server;

	@Inject(
		method = "onLogin",
		at = @At(
			value = "NEW",
			target = "net/minecraft/network/packet/s2c/play/LoginS2CPacket"
		)
	)
	private void osl$idsync$syncRegistries(CallbackInfo ci, @Local ServerPlayerEntity player) {
		if (this.server.isDedicated() || !this.server.getUsername().equals(player.getName())) {
			ServerPlayNetworking.sendNoCheck(player, Constants.OSL_REGISTRY_SYNC_CHANNEL, SyncedRegistriesPacketSerializer::serialize);
		}
	}
}
