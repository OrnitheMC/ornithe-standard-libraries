package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.entity.mob.player.LocalClientPlayerEntity;
import net.minecraft.client.network.handler.ClientNetworkHandler;

import net.ornithemc.osl.networking.impl.access.LocalClientPlayerAccess;

@Mixin(LocalClientPlayerEntity.class)
public class LocalClientPlayerEntityMixin implements LocalClientPlayerAccess {

	@Shadow
	private ClientNetworkHandler networkHandler;

	@Override
	public ClientNetworkHandler osl$networking$getNetworkHandler() {
		return this.networkHandler;
	}
}
