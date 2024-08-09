package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.entity.mob.player.LocalClientPlayerEntity;
import net.minecraft.client.network.handler.ClientNetworkHandler;

@Mixin(LocalClientPlayerEntity.class)
public interface LocalClientPlayerEntityAccessor {

	@Accessor("networkHandler")
	default ClientNetworkHandler getNetworkHandler() {
		throw new UnsupportedOperationException();
	}
}
