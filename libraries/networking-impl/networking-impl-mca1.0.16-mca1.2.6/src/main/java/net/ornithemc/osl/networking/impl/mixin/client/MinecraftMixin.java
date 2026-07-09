package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.mob.player.ClientPlayerEntity;
import net.minecraft.client.world.MultiplayerWorld;
import net.minecraft.world.World;

import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.access.ClientNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.LocalClientPlayerAccess;
import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	private World world;
	@Shadow
	private ClientPlayerEntity player;

	@Inject(
		method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$disconnect(World world, String title, CallbackInfo ci) {
		if (this.world != null && this.world instanceof MultiplayerWorld && world == null) {
			LocalClientPlayerAccess player = (LocalClientPlayerAccess) this.player;
			ClientNetworkHandlerAccess networkHandler = (ClientNetworkHandlerAccess) player.osl$networking$getNetworkHandler();
			ClientConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

			ClientConnectionEvents.DISCONNECT.invoker().accept(connectionContext);
		}
	}
}
