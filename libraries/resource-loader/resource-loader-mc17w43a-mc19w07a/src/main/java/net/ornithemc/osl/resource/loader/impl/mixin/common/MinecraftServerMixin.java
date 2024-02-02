package net.ornithemc.osl.resource.loader.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.resource.loader.api.server.ServerResourceLoaderEvents;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(
		method = "reloadResources",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$startResourceReload(CallbackInfo ci) {
		ServerResourceLoaderEvents.START_RESOURCE_RELOAD.invoker().run();
	}

	@Inject(
		method = "reloadResources",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$endResourceReload(CallbackInfo ci) {
		ServerResourceLoaderEvents.END_RESOURCE_RELOAD.invoker().run();
	}
}
