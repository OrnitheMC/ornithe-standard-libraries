package net.ornithemc.osl.config.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.world.World;

import net.ornithemc.osl.config.impl.ConfigInitializer;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow private World world;

	@Inject(
		method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/entity/living/player/PlayerEntity;)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$config$closeWorld(World world, String message, PlayerEntity player, CallbackInfo ci) {
		if (this.world != null && !this.world.isMultiplayer && world == null && osl$config$startGameDepth == 0) {
			ConfigInitializer.CLOSE_WORLD.invoker().accept((Minecraft)(Object)this);
		}
	}

	@Unique private int osl$config$startGameDepth;

	@Inject(
		method = "startGame",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$config$loadWorld(String name, String saveName, long seed, CallbackInfo ci) {
		if (osl$config$startGameDepth++ == 0) {
			// The startGame method recursively calls itself when converting from
			// older world formats, but we only want to capture the initial call.
			ConfigInitializer.START_GAME.invoker().accept((Minecraft)(Object)this, saveName);
		}
	}

	@Inject(
		method = "startGame",
		at = @At(
			value = "RETURN"
		)
	)
	private void osl$config$readyWorld(CallbackInfo ci) {
		osl$config$startGameDepth--;
	}
}
