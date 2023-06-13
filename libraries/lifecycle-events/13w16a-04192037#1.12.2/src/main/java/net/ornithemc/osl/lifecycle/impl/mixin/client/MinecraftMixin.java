package net.ornithemc.osl.lifecycle.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;

import net.ornithemc.osl.lifecycle.api.client.ClientWorldEvents;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startClient(CallbackInfo ci) {
		MinecraftClientEvents.START.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$readyClient(CallbackInfo ci) {
		MinecraftClientEvents.READY.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "stop",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$stopClient(CallbackInfo ci) {
		MinecraftClientEvents.STOP.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick(CallbackInfo ci) {
		MinecraftClientEvents.TICK_START.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$endTick(CallbackInfo ci) {
		MinecraftClientEvents.TICK_END.invoker().accept((Minecraft)(Object)this);
	}

	@Inject(
		method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$readyClientWorld(ClientWorld world, String s, CallbackInfo ci) {
		if (world != null) {
			ClientWorldEvents.READY.invoker().accept(world);
		}
	}
}
