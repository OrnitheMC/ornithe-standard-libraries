package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.mob.player.ClientPlayerEntity;
import net.minecraft.client.entity.mob.player.LocalClientPlayerEntity;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.world.World;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.Networking;
import net.ornithemc.osl.networking.impl.access.ClientNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.access.MinecraftAccess;
import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;

@Mixin(Minecraft.class)
public class MinecraftMixin implements MinecraftAccess {

	@Conditional(minecraftVersion = @Version("<12w18a"))
	@Shadow
	private ClientPlayerEntity f_28396371;
	@Conditional(minecraftVersion = @Version(">=12w18a"))
	@Shadow
	private LocalClientPlayerEntity player;
	@Conditional(minecraftVersion = @Version("<12w18a"))
	@Shadow
	private World f_26407825;
	@Conditional(minecraftVersion = @Version(">=12w18a"))
	@Shadow
	private ClientWorld world;

	@Shadow
	private ClientNetworkHandler getNetworkHandler() { return null; }

	@Conditional(minecraftVersion = @Version("<12w18a"))
	@Inject(
		method = "m_04848709",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$disconnect(World world, String title, PlayerEntity player, CallbackInfo ci) {
		if (this.f_26407825 != null && this.f_26407825 instanceof ClientWorld && world == null) {
			this.triggerDisconnectEvent();
		}
	}

	@Conditional(minecraftVersion = @Version(">=12w18a"))
	@Inject(
		method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$disconnect(ClientWorld world, String title, CallbackInfo ci) {
		if (this.world != null && world == null) {
			this.triggerDisconnectEvent();
		}
	}

	@Unique
	private void triggerDisconnectEvent() {
		ClientNetworkHandlerAccess networkHandler = (ClientNetworkHandlerAccess) this.getNetworkHandler();
		ClientConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

		ClientConnectionEvents.DISCONNECT.invoker().accept(connectionContext);
	}

	@Override
	public boolean osl$networking$hasWorldAndPlayer() {
		if (Networking.LOCAL_SERVER_EXISTS) {
			return this.world != null && this.player != null;
		} else {
			return this.f_26407825 != null && this.f_28396371 != null;
		}
	}
}
